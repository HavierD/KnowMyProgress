package tech.havier.databaseOperator.hibernate.entities;


import javax.persistence.*;

@Entity
@Table(name = "IGNORE_DICTIONARY")
public class IgnoredWord {

    @Id
    @Column(name = "WORD")
    private String word;

    public IgnoredWord(String word) {
        setWord(word);
    }

    public IgnoredWord() {

    }

    public String getWord() {
        return word;
    }

    public void setWord(String word) {
        this.word = word;
    }
}
