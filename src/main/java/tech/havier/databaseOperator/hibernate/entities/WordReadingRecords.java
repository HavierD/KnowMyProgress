package tech.havier.databaseOperator.hibernate.entities;

import javax.persistence.*;

@Entity
@Table(name = "WORDS_READING_RECORDS")
public class WordReadingRecords {


    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "c_generator")
    @SequenceGenerator(name = "c_generator", sequenceName = "SEQ_RECORDS", allocationSize = 1)
    private Integer id;

    @Column(name = "WORD")
    private String word;

    @Column(name = "year")
    private String year;

    @Column(name = "month")
    private String month;

    @Column(name = "day")
    private String day;

    public WordReadingRecords(String word, String year, String month, String day) {
        this.word = word;
        this.year = year;
        this.month = month;
        this.day = day;
    }

    public WordReadingRecords() {

    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getWord() {
        return word;
    }

    public void setWord(String word) {
        this.word = word;
    }

    public String getYear() {
        return year;
    }

    public void setYear(String year) {
        this.year = year;
    }

    public String getMonth() {
        return month;
    }

    public void setMonth(String month) {
        this.month = month;
    }

    public String getDay() {
        return day;
    }

    public void setDay(String day) {
        this.day = day;
    }
}
