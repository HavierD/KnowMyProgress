package tech.havier;

import org.omg.CORBA.StringHolder;

public class WordElement {
    private String word = null;
    private String partOfSpeech = null;
    private String cefrLevels = null;
    private String repeatTimes = null;
    private String firstMeetTime = null;
    private String lastMeetTime = null;
    private String currentState = null;
    private String formulaS = null;

    public WordElement(String word, String repeatTimes) {
        this.word = word;
        this.repeatTimes = repeatTimes;
    }

    public boolean contains(String word) {
        if (word == this.word) {
            return true;
        }
        return false;
    }

    public String getRepeatTimes() {
        return repeatTimes;
    }

    public String getWord() {
        return word;
    }
}
/**
 * 单词，词性，单词列表范围（A1 A2...C1 C2）,遇见次数，首次遇见时间， 最近一次遇见时间，  当前熟悉状态，
 * r= exp(-t/s)来计算当前熟悉的状态。
 * 时而的单词检查,来计算一个通用s，和单词自身的s
 *
 * 检查： 数据库有没有，没有：检查learnerdictionary，
 */
