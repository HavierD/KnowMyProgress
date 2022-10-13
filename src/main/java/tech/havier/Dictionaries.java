package tech.havier;

import tech.havier.databaseOperator.DatabaseOperator;
import tech.havier.databaseOperator.DatabaseFactory;

import java.util.HashMap;
import java.util.List;

public class Dictionaries {

    private final HashMap<String, String> wordTransitionDictionary;
    private final List<String> wordDictionary;
    private final List<String> ignoreDictionary;
//    private final HashMap<String, String> suffixConvertDictionary = new HashMap<>();
    private String[][] suffixConvertDictionary;


    public Dictionaries() throws Exception {
        DatabaseOperator databaseOperator = new DatabaseFactory().createDatabaseOperator();
        this.wordTransitionDictionary = databaseOperator.getTransitionDictionary();
        this.wordDictionary = databaseOperator.getWordDictionary();
        this.ignoreDictionary = databaseOperator.getIgnoreDictionary();
        initializeSuffixConvertDictionary();

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

    public boolean hasExistingAasB(String word) {
        word = formatWord(word);
        if (wordTransitionDictionary.containsKey(word)) {
            return true;
        }
        return false;
    }

    public String[][] getSuffixConvertDictionary() {
        return suffixConvertDictionary;
    }

    private String formatWord(String rawWord) {
        rawWord = rawWord.toLowerCase();
        rawWord = rawWord.replaceAll(" ","");
        return rawWord;
    }


    private void initializeSuffixConvertDictionary() {

        suffixConvertDictionary = new String[][]{
                {"ies", "y"},
                {"ves","f"},
                {"es",""},
                {"s",""},
                {"ying","ie"},
                {"ing", ""},
                {"ed", ""},
                {"ed", "e"},
                {"ied", "y"}
        };
    }



}
