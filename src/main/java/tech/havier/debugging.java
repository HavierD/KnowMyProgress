package tech.havier;

import tech.havier.databaseOperator.ConfigHavi1;
import tech.havier.timeToolkit.HavierTimer;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class debugging {

    static ConfigHavi1 config = new ConfigHavi1();

    public static void main(String[] args) throws Exception {
//        testNoIndexPerformance();
//        testIndexPerformance();


        List<Integer> intTest = new ArrayList<>();
        intTest.add(2);
        intTest.add(1);
        intTest.add(5);
        intTest.add(8);
        intTest.add(1);
        intTest.add(3);
        intTest.add(9);



        System.out.println(intTest);

        intTest = intTest.stream().filter(a->a<5).collect(Collectors.toList());
        System.out.println(intTest);
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
