package tech.havier;

import tech.havier.databaseOperator.DatabaseDelegate;

import java.util.HashMap;
import java.util.List;

public class Dictionaries {

    private final HashMap<String, String> wordTransitionDictionary;
    private final List<String> wordDictionary;
    private final List<String> ignoreDictionary;


    public Dictionaries() throws Exception {
        DatabaseDelegate databaseDelegate = new DatabaseDelegate();
        this.wordTransitionDictionary = databaseDelegate.getTransitionDictionary();
        this.wordDictionary = databaseDelegate.getWordDictionary();
        this.ignoreDictionary = databaseDelegate.getIgnoreDictionary();
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

    public boolean doesIgnore(String word) {
        word = formatWord(word);
        if(ignoreDictionary.contains(word)){
            return true;
        }
        return false;
    }


    private String formatWord(String rawWord) {
        rawWord = rawWord.toLowerCase();
        rawWord = rawWord.replaceAll(" ","");
        return rawWord;
    }


}
