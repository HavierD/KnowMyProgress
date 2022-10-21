package tech.havier.databaseOperator.hibernate.entities;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "WORDS_LIST")
public class WordsList {

    @Id
    @Column(name = "WORD")
    private String word;

    @Column(name = "PART_OF_SPEECH")
    private String partOfSpeech;

    @Column(name = "CEFR_LEVELS")
    private String cefrLevels;

    @Column(name = "REPETITION")
    private int repetition;

    @Column(name = "FIRST_MEET_TIME")
    private String firstMeetTime;

    @Column(name = "LAST_MEET_TIME")
    private String lastMeetTime;

    @Column(name = "CURRENT_STATE")
    private String currentState;

    @Column(name = "FORMULA_S")
    private String formulaS;

    public WordsList() {
    }

    public WordsList(String word, String lastMeetTime) {
        this.word = word;
        this.lastMeetTime = lastMeetTime;
        this.repetition = 1;
    }

    public String getWord() {
        return word;
    }

    public void setWord(String word) {
        this.word = word;
    }

    public String getPartOfSpeech() {
        return partOfSpeech;
    }

    public void setPartOfSpeech(String partOfSpeech) {
        this.partOfSpeech = partOfSpeech;
    }

    public String getCefrLevels() {
        return cefrLevels;
    }

    public void setCefrLevels(String cefrLevels) {
        this.cefrLevels = cefrLevels;
    }

    public int getRepetition() {
        return repetition;
    }

    public void setRepetition(int repetition) {
        this.repetition = repetition;
    }

    public String getFirstMeetTime() {
        return firstMeetTime;
    }

    public void setFirstMeetTime(String firstMeetTime) {
        this.firstMeetTime = firstMeetTime;
    }

    public String getLastMeetTime() {
        return lastMeetTime;
    }

    public void setLastMeetTime(String lastMeetTime) {
        this.lastMeetTime = lastMeetTime;
    }

    public String getCurrentState() {
        return currentState;
    }

    public void setCurrentState(String currentState) {
        this.currentState = currentState;
    }

    public String getFormulaS() {
        return formulaS;
    }

    public void setFormulaS(String formulaS) {
        this.formulaS = formulaS;
    }
}
