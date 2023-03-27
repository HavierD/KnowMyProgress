package tech.havier.databaseOperator.sql;

import tech.havier.databaseOperator.ConfigHavi1;
import tech.havier.databaseOperator.DatabaseOperator;
import tech.havier.stringBlockDelegate.StringBlockImporter;
import tech.havier.timeToolkit.HavierDate;
import tech.havier.timeToolkit.HavierTimer;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class SqlOperator implements DatabaseOperator {

    private static final ConfigHavi1 config = new ConfigHavi1();

    private final HashMap<String, String> transitionDictionary = new HashMap<>();
    private final List<String> wordDictionary = new ArrayList<>();
    private final List<String> ignoreDictionary = new ArrayList<>();
    private static HavierDate date = new HavierDate();

    private static SqlOperator sqlOperator;

    HavierTimer timer = new HavierTimer();




    public static SqlOperator getSqlOperatorInstance() throws Exception {
        if (sqlOperator == null) {
            sqlOperator = new SqlOperator();
        }
        if(StringBlockImporter.isDatabaseNeedRefresh()){
            refreshDatabase();
        }
        return sqlOperator;
    }

    private static void refreshDatabase() throws Exception {
        sqlOperator = new SqlOperator();
    }

    private SqlOperator() throws Exception {
        initializeWordDictionaryFromSql();
        initializeTransitionDictionaryFromSql();
        initializeIgnoreDictionaryFromSql();
        date = new HavierDate();
    }

    public HashMap<String, String> getTransitionDictionary() {
        return transitionDictionary;
    }

    public List<String> getWordDictionary() {
        return wordDictionary;
    }

    public void uploadNewAasB(String key, String value) {
        try {
            Class.forName("oracle.jdbc.driver.OracleDriver");
            Connection connection = getConnection();
            Statement statement = connection.createStatement();
            statement.executeUpdate("insert into a_as_b  values ('" + key + "','" + value + "')");
            connection.close();

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void uploadNewAasBs(ConcurrentHashMap<String, String> pairs) {
        for(Map.Entry<String, String> set : pairs.entrySet()){
            uploadNewAasB(set.getKey(), set.getValue());
        }
    }

    public void uploadNewIgnoredWord(String word) {
        try {
            Class.forName("oracle.jdbc.driver.OracleDriver");
            Connection connection = getConnection();
            Statement statement = connection.createStatement();
            statement.executeUpdate("insert into ignore_dictionary  values ('" + word + "')");
            connection.close();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void uploadNewIgnoredWords(List<String> words) {
        for (String word : words) {
            uploadNewIgnoredWord(word);
        }
    }

    public List<String> getIgnoreDictionary() {
        return this.ignoreDictionary;
    }

    public void updateWord(String word) throws SQLException, ClassNotFoundException {
        try {
            Class.forName("oracle.jdbc.driver.OracleDriver");
            Connection connection = getConnection();
            Statement statement = connection.createStatement();
            statement.executeUpdate("update words_list set last_meet_time='" +date.getCurrentWholeDate() +  "' where word='" + word + "'" );
        } catch (Exception e) {
            throw e;
        }
        uploadNewRecord(word);
    }

    public void uploadNewWord(String word) {
        try {
            Class.forName("oracle.jdbc.driver.OracleDriver");
            Connection connection = getConnection();
            Statement statement = connection.createStatement();
            String query = String.format("" +
                    "merge into words_list dest " +
                    "using (select '%1$s' word from dual) src " +
                    "on (dest.word = src.word) " +
                    "when matched then update set repetition = repetition + 1, last_meet_time = '%2$s' " +
                    "when not matched then insert(word, repetition, last_meet_time) values (src.word, 1, '%2$s')",
                    word, date.getCurrentWholeDate());
            statement.executeUpdate(query);
            connection.close();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        uploadNewRecord(word);
    }

    @Override
    public void uploadNewWords(List<String> words) {
        for (String word : words) {
            uploadNewWord(word);
        }
    }

    public void uploadNewRecord(String word) {
        try {
            Class.forName("oracle.jdbc.driver.OracleDriver");
            Connection connection = getConnection();
            Statement statement = connection.createStatement();
            statement.executeUpdate("insert into words_reading_records  values ('" + word + "'," + date.getCurrentYear() + "," + date.getCurrentMonth() +"," +
                    date.getCurrentDate()+ ", seq_records.nextval)");


        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void uploadNewRecords(List<String> words) {
        for (String word : words) {
            uploadNewRecord(word);
        }
    }

    @Override
    public List<String> getWordDictionaryByRepetitions() {
        return null;
    }

    private void initializeWordDictionaryFromSql() throws Exception {
        try {
            timer.start(0);
            Connection connection = getConnection();
            //populate word dictionary
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery("select * from words_list order by word");
            while (resultSet.next()) {
                String word = resultSet.getString(1);
                wordDictionary.add(word);
            }
            timer.cancel("Word Dictionary from SQL");
        } catch (Exception e) {
            throw new Exception(e);
        }
    }

    private void initializeTransitionDictionaryFromSql() throws Exception {
        try{
            timer.start(0);
            Connection connection = getConnection();
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery("select * from A_AS_B order by BEFORE_WORD");

            while (resultSet.next()) {
                transitionDictionary.put(resultSet.getString(1), resultSet.getString(2));
            }
            timer.cancel("Transition Dictionary from SQL");

        } catch (Exception e) {
            throw new Exception(e);
        }

    }

    private void initializeIgnoreDictionaryFromSql() throws Exception {
        try{
            timer.start(0);
            Connection connection = getConnection();
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery("select * from ignore_dictionary order by WORD");
            while (resultSet.next()) {
                ignoreDictionary.add(resultSet.getString(1));
            }
            timer.cancel("Ignore dictionary from SQL");

        } catch (Exception e) {
            throw new Exception(e);
        }
    }
    private static Connection getConnection() throws ClassNotFoundException, SQLException {
        Class.forName("oracle.jdbc.driver.OracleDriver");
        Connection connection = DriverManager.getConnection(
                "jdbc:oracle:thin:@localhost:1521/XEPDB1",
                config.a, config.b );
        return connection;
    }

}
