package tech.havier.databaseOperator.hibernate.entities;


import javax.persistence.*;

@Entity
@Table(name = "IGNORE_DICTIONARY")
public class IgnoreDictionary {

    @Id
    @Column(name = "WORD")
    private String word;

    public String getWord() {
        return word;
    }

    public void setWord(String word) {
        this.word = word;
    }
}
