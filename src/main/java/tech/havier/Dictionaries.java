package tech.havier;

import tech.havier.databaseOperator.DatabaseOperator;
import tech.havier.databaseOperator.DatabaseFactory;

import java.util.*;
import java.util.stream.Collectors;

public class Dictionaries {

    private final HashMap<String, String> wordTransitionDictionary;
    private final List<String> selfTransitionedDictionary = new ArrayList<>();
    private List<String> temporarySelfTransWords = new ArrayList<>();
    private final List<String> wordDictionary;
    private final List<String> ignoreDictionary;
//    private final HashMap<String, String> suffixConvertDictionary = new HashMap<>();
    private String[][] suffixConvertDictionary;


    public List<String> getWordDictionaryByRepetitions() throws Exception {
        DatabaseOperator databaseOperator = new DatabaseFactory().createDatabaseOperator();
        return databaseOperator.getWordDictionaryByRepetitions();
    }

    public Dictionaries() throws Exception {
        DatabaseOperator databaseOperator = new DatabaseFactory().createDatabaseOperator();
        this.wordTransitionDictionary = databaseOperator.getTransitionDictionary();
        this.wordDictionary = databaseOperator.getWordDictionary();
        this.ignoreDictionary = databaseOperator.getIgnoreDictionary();
        initializeSelfTransDic();
        initializeSuffixConvertDictionary();
    }

    public List<String> getTemporarySelfTransWords() {
        return temporarySelfTransWords;
    }

    public String transitions(String rawWord) {
        rawWord = formatWord(rawWord);
        if(!wordTransitionDictionary.containsKey(rawWord)) return rawWord;
        return wordTransitionDictionary.get(rawWord);
    }

    public List<String> transitions(List<String> strings) {
        return strings.stream()
                .map(e->{
                    if(selfTransitionedDictionary.contains(e)) {
                        temporarySelfTransWords.add(e);
                        return null;
                    }
                    return e;
                })
                .filter(Objects::nonNull)
                .map(this::transitions).collect(Collectors.toList());
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
        return ignoreDictionary.contains(word);
    }

    public boolean hasExistingAasB(String word) {
        word = formatWord(word);
        return wordTransitionDictionary.containsKey(word);
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
    private void initializeSelfTransDic() {
        for (Map.Entry<String, String> entry : wordTransitionDictionary.entrySet()) {
            if (!entry.getKey().equals(entry.getValue())) continue;
            selfTransitionedDictionary.add(entry.getKey());
        }
    }



}
