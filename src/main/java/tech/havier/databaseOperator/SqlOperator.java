package tech.havier.databaseOperator;

import tech.havier.Date;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class SqlOperator {

    private static final ConfigHavi1 config = new ConfigHavi1();

    private final HashMap<String, String> transitionDictionary = new HashMap<>();
    private final List<String> wordDictionary = new ArrayList<>();
    private final List<String> ignoreDictionary = new ArrayList<>();
    private static Date date = new Date();


    public static void main(String[] args) throws SQLException, ClassNotFoundException {

    }




    public SqlOperator() throws Exception {
        initializeTransitionDictionaryFromSql();
        initializeWordDictionaryFromSql();
        initializeIgnoreDictionaryFromSql();
        date = new Date();
    }

    private void initializeWordDictionaryFromSql() throws Exception {
        try {
            Connection connection = getConnection();

            //populate word dictionary
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery("select * from words_list order by word");
            while (resultSet.next()) {
                String word = resultSet.getString(1);
                wordDictionary.add(word);
            }

            //populate transition dictionary
            Statement statement2 = connection.createStatement();
            ResultSet resultSet2 = statement2.executeQuery("select * from a_as_b order by before_word");
            while (resultSet2.next()) {
                String beforeWord = resultSet2.getString(1);
                String afterWord = resultSet2.getString(2);
                transitionDictionary.put(beforeWord, afterWord);
            }

        } catch (Exception e) {
            throw new Exception(e);
        }
    }


    private void initializeTransitionDictionaryFromSql() throws Exception {
        try{
            Connection connection = getConnection();
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery("select * from A_AS_B order by BEFORE_WORD");

            while (resultSet.next()) {
                transitionDictionary.put(resultSet.getString(1), resultSet.getString(2));
            }

        } catch (Exception e) {
            throw new Exception(e);
        }

    }
    private void initializeIgnoreDictionaryFromSql() throws Exception {
        try{
            Connection connection = getConnection();
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery("select * from ignore_dictionary order by WORD");
            while (resultSet.next()) {
                ignoreDictionary.add(resultSet.getString(1));
            }
        } catch (Exception e) {
            throw new Exception(e);
        }
    }

    public HashMap<String, String> getTransitionDictionary() {
        return transitionDictionary;
    }

    public List<String> getWordDictionary() {
        return wordDictionary;
    }

    private static Connection getConnection() throws ClassNotFoundException, SQLException {
        Class.forName("oracle.jdbc.driver.OracleDriver");
        Connection connection = DriverManager.getConnection(
                "jdbc:oracle:thin:@localhost:1521/XEPDB1",
                config.a, config.b );
        return connection;
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
        addNewRecord(word);
    }

    public void addNewWord(String word) {
        try {
            Class.forName("oracle.jdbc.driver.OracleDriver");
            Connection connection = getConnection();
            Statement statement = connection.createStatement();
            statement.executeUpdate("insert into words_list (word, last_meet_time) values ('" + word  + "','" + date.getCurrentWholeDate() + "')");

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        addNewRecord(word);
    }

    private void addNewRecord(String word) {
        try {
            Class.forName("oracle.jdbc.driver.OracleDriver");
            Connection connection = getConnection();
            Statement statement = connection.createStatement();
            statement.executeUpdate("insert into words_reading_records  values ('" + word + "'," + date.getCurrentYear() + "," + date.getCurrentMonth() +"," +
                    date.getCurrentDate()+ ")");


        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public void addNewAasB(String key, String value) {
        try {
            Class.forName("oracle.jdbc.driver.OracleDriver");
            Connection connection = getConnection();
            Statement statement = connection.createStatement();
            statement.executeUpdate("insert into a_as_b  values ('" + key + "','" + value + "')");

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }



    public void uploadNewIgnoredWord(String word) {
        try {
            Class.forName("oracle.jdbc.driver.OracleDriver");
            Connection connection = getConnection();
            Statement statement = connection.createStatement();
            statement.executeUpdate("insert into ignore_dictionary  values (' " + word + " ')");

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public List<String> getIgnoreDictionary() {
        return this.ignoreDictionary;
    }
}
