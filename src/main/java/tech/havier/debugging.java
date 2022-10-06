package tech.havier;

import tech.havier.databaseOperator.ConfigHavi1;
import tech.havier.timeToolkit.HavierTimer;

import java.sql.*;

public class debugging {

    static ConfigHavi1 config = new ConfigHavi1();

    public static void main(String[] args) throws Exception {
        testNoIndexPerformance();
        testIndexPerformance();

    }

    private static void testIndexPerformance() throws Exception {
        HavierTimer timer = new HavierTimer();
        try {
            timer.start(0);
            Connection connection = getConnection();
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery("select * from words_reading_records where 'year' = '2022'");
            timer.cancel("pretending using index");
        } catch (Exception e) {
            throw new Exception(e);
        }
    }

    private static void testNoIndexPerformance() throws Exception {
        HavierTimer timer = new HavierTimer();
        try {
            timer.start(0);
            Connection connection = getConnection();
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery("select * from words_reading_records where 'year + 1 '= '2021'");
            timer.cancel("pretending not using index");
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
