package tech.havier.databaseOperator.sql;

import tech.havier.databaseOperator.ConfigHavi1;

import java.sql.*;
import java.util.List;

public class SqlOperations {

    private static ConfigHavi1 config = new ConfigHavi1();
    public static void main(String[] args) throws Exception {
        sql();
    }

    /**
     * update repetitions of words in the word list.
     * @throws Exception
     */
    private static void sql() throws Exception {

        List<String> dictionaries = SqlOperator.getSqlOperatorInstance().getWordDictionary();
        try {
            Class.forName("oracle.jdbc.driver.OracleDriver");

            for (String e : dictionaries) {
                Connection connection = DriverManager.getConnection(
                        "jdbc:oracle:thin:@localhost:1521/XEPDB1",
                        config.a, config.b );
                Statement statement = connection.createStatement();
                ResultSet resultSet = statement.executeQuery("select count(*) from words_reading_records where word='"+ e + "'");
                resultSet.next();
                int repetition = resultSet.getInt(1);

                Statement statement2 = connection.createStatement();
                statement2.executeUpdate("update words_list set repetition=" + repetition + " where word='"+e+"'");
                connection.close();
            }


        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }


}
