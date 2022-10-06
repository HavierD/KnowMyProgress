package tech.havier.timeToolkit;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class HavierDate {

    private static String currentDate;
    private String currentMonth;
    private String currentYear;

    public static void main(String[] args) {
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd");
        LocalDateTime now = LocalDateTime.now();
        currentDate = dtf.format(now);
        System.out.println(currentDate);
    }

    public HavierDate() {
        DateTimeFormatter date = DateTimeFormatter.ofPattern("dd");
        DateTimeFormatter month = DateTimeFormatter.ofPattern("MM");
        DateTimeFormatter year = DateTimeFormatter.ofPattern("yyyy");
        LocalDateTime now = LocalDateTime.now();
        currentDate = date.format(now);
        currentMonth = month.format(now);
        currentYear = year.format(now);

    }

    public String getCurrentDate() {
        return currentDate;
    }

    public String getCurrentMonth() {
        return currentMonth;
    }

    public String getCurrentYear() {
        return currentYear;
    }

    public String getCurrentWholeDate() {
        String wholeDate = currentDate + "/" + currentMonth + "/" + currentYear;
        return wholeDate;
    }
}
