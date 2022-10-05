package tech.havier;

import tech.havier.databaseOperator.SqlOperator;

import java.util.HashMap;
import java.util.List;

public class Dictionaries {
    private HashMap<String, String> wordTransitionDictionary;
    private List<String> wordDictionary;
    private List<String> ignoreDictionary;

    public Dictionaries() throws Exception {
        SqlOperator sqlOperator = new SqlOperator();
        this.wordTransitionDictionary = sqlOperator.getTransitionDictionary();
        this.wordDictionary = sqlOperator.getWordDictionary();
        this.ignoreDictionary = sqlOperator.getIgnoreDictionary();
    }

    public String transitions(String rawWord) {
        rawWord = formatWord(rawWord);
        if (wordTransitionDictionary.keySet().contains(rawWord)) {
            return wordTransitionDictionary.get(rawWord);
        }
        return null;
    }

    public boolean hasSaved(String rawWord) {
        rawWord = formatWord(rawWord);
        for (String e : wordDictionary) {
            if (e.equals(rawWord)) {
                return true;
            }
        }
        return false;
    }

    private String formatWord(String rawWord) {
        rawWord = rawWord.toLowerCase();
        rawWord = rawWord.replaceAll(" ","");
        return rawWord;
    }

    public boolean doesIgnore(String word) {
        word = formatWord(word);
        if(ignoreDictionary.contains(word)){
            return true;
        }
        return false;
    }
}
