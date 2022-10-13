package tech.havier.databaseOperator.hibernate.entities;


import javax.persistence.*;

@Entity
@Table(name = "A_AS_B")
@Access(AccessType.FIELD)
public class AasB {

    @Id
    @Column(name = "BEFORE_WORD")
    private String beforeWord;

    @Column(name = "AFTER_WORD")
    private String afterWord;

    public AasB(String beforeWord, String afterWord) {
        this.beforeWord = beforeWord;
        this.afterWord = afterWord;
    }

    public AasB() {
    }

    public String getBeforeWord() {
        return beforeWord;
    }

    public void setBeforeWord(String beforeWord) {
        this.beforeWord = beforeWord;
    }

    public String getAfterWord() {
        return afterWord;
    }

    public void setAfterWord(String afterWord) {
        this.afterWord = afterWord;
    }
}
