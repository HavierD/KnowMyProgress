package tech.havier.stringBlockOperator;


import tech.havier.Dictionaries;
import tech.havier.PerformanceMonitor;
import tech.havier.databaseOperator.DatabaseFactory;
import tech.havier.databaseOperator.DatabaseOperator;
import tech.havier.stringBlockDelegate.StringBlockImporter;
import tech.havier.timeToolkit.HavierTimer;

import java.sql.SQLException;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

public class StringBlockOperator1 implements StringBlockOperator {

    private List<String> ignoredWords = new ArrayList<>();
    private final List<String> toRemove = new ArrayList<>();
    private final List<String> foundWords = new ArrayList<>();
    private final ConcurrentHashMap<String, String> convertedWords = new ConcurrentHashMap<>();
    private List<String> unknownWords = new ArrayList<>();

    private HavierTimer timer = new HavierTimer();

    private Dictionaries dictionaries;
    private DatabaseOperator databaseOperator;

    private PerformanceMonitor performanceMonitor = new PerformanceMonitor();

    public StringBlockOperator1(StringBlockImporter stringBlockImporter) throws Exception {

        this.dictionaries = new Dictionaries();
        this.databaseOperator = new DatabaseFactory().createDatabaseOperator();

        String string = stringBlockImporter.getStringBlock();
        List<String> wordsWithSpace = parseLongStringIntoList(string);
        List<String> wordsNoSpace = replaceBlankString(wordsWithSpace);
        List<String> transitionedWords = tryToTransition(wordsNoSpace);
        validateWords(transitionedWords);
        performanceMonitor.proportionOfSearchingOnline();
        finalChecking();
    }



    public int savedWordsNumber() {return foundWords.size();}
    public int needCheckWordsNumber(){return convertedWords.size();}
    public int unknownWordsNumber(){return unknownWords.size();}

    public void printSavedWords() {
        int index = 0;
        for (String e : foundWords) {
            System.out.println(index + ". " + e);
            index++;
        }
    }

    public void printConvertedWords() {
        int index = 0;
        for (Map.Entry<String, String> e : convertedWords.entrySet()) {
            System.out.println(index + ". " + e.getKey() + " : " + e.getValue());
            index++;
        }
    }

    public void printUnknownWords() {
        int index = 0;
        for (String e : unknownWords) {
            System.out.println(index + ". " + e);
            index++;
        }
    }

    public void uploadFoundWords() {
        timer.start(0);
        int index = 0;
        for (String e : foundWords) {
            index++;
            if (index % 100 == 0)System.out.println("uploading " + index + " / " + foundWords.size());
            databaseOperator.uploadNewWord(e);
        }
        timer.cancel("Uploading found words ");
        foundWords.clear();
        System.out.println("All found words upload successfully! Found-word-list has been cleared.");
    }

    public void bunchIgnore(Scanner scanner, int[] indexes) {
        System.out.println("Are you sure to ignore below word(s)? y/ or anything others to cancel");
        for (int e : indexes) {
            System.out.print(e + ". ");
            System.out.println(foundWords.get(e));
        }
        String input = scanner.nextLine();
        if(input.equals("")){
            List<String> toRemove = new ArrayList<>();
            for (int e : indexes) {
                databaseOperator.uploadNewIgnoredWord(foundWords.get(e));
                toRemove.add(foundWords.get(e));
            }
            foundWords.removeAll(toRemove);
        }
    }

    public void uploadCurrentAasBDictionary() {
        for (Map.Entry<String, String> e : convertedWords.entrySet()){
            if (dictionaries.hasExistingAasB(e.getKey())) {
                databaseOperator.uploadNewRecord(e.getValue());
            }
            databaseOperator.uploadNewAasB(e.getKey(), e.getValue());
        }
        System.out.println("Current converted words uploaded successfully!");
        convertedWords.clear();
    }

    private void finalChecking() {
        unknownWords = unknownWords.stream().filter(w->!foundWords.contains(w)).collect(Collectors.toList());
    }


    /**
     * replace all punctuation into space.
     * @param string
     * @return
     */
    private List<String> parseLongStringIntoList(String string) {
        String s1 = string
                .replaceAll("'s", " ")
                .replaceAll("\\p{Punct}", " ")
                .replaceAll("[0-9]", "");
        List<String> stringList = Arrays.asList(s1.split(" "));
        List<String> returnedList = splitCamelCaseWords(stringList);
        return returnedList;
    }

    private List<String> splitCamelCaseWords(List<String> stringList) {
        List<String> returnedList = new ArrayList<>();
        for (String e : stringList) {
            if(upperCaseLocation(e) == null){
                returnedList.add(e.toLowerCase());
            }
            else{
                returnedList.add(e.substring(0, upperCaseLocation(e).get(0)).toLowerCase());
                for (int i = 1; i < upperCaseLocation(e).size(); i++){
                    returnedList.add(e.substring(upperCaseLocation(e).get(i-1), upperCaseLocation(e).get(i)-1).toLowerCase());
                }
                returnedList.add(e.substring(upperCaseLocation(e).get(upperCaseLocation(e).size()-1)).toLowerCase());
            }
        }
        return returnedList;
    }

    private List<String> replaceBlankString(List<String> wordsWithSpace) {
        List<String> wordsNoSpace = new ArrayList<>();
        for (String e : wordsWithSpace) {
            if (replaceWhiteSpace(e).equals(""))continue;
            wordsNoSpace.add(e);
        }
        return wordsNoSpace;
    }

    private String replaceWhiteSpace(String string) {
        String string1 = string.replaceAll(" ", "");
        return string1;

    }

    private List<String> tryToTransition(List<String> list) {
        List<String> returnedList = new ArrayList<>();
        for (String e : list) {
                returnedList.add(dictionaries.transitions(e));
        }
        return returnedList;
    }


    private void validateWords(List<String> wordsCheckedAasB) {
        int process = 0;
        for (String e : wordsCheckedAasB) {
            process++;
            if (process % 100 == 0) {
                System.out.println("validating process: " + process + " / " + wordsCheckedAasB.size());
            }
            if(dictionaries.doesIgnore(e)) continue;
            if (isKnownWord(e)) continue;
            if (isOnWeb(e)) continue;
            boolean isConverted = false;
            for (String[] strings : dictionaries.getSuffixConvertDictionary()) {
                if (doesEndWith(e, strings)) {
                    isConverted = true;
                    break;
                }
            }
            if(isConverted) continue;
            if(unknownWords.contains(e))continue;
            unknownWords.add(e);
        }
    }

    private boolean doesEndWith(String checkedWord, String[] suffixPair) {
        int charCount = suffixPair[0].length();
        if (checkedWord.endsWith(suffixPair[0])) {
            String afterWord = checkedWord.substring(0, checkedWord.length() - charCount) + suffixPair[1];
            if (dictionaries.hasSaved(afterWord) || searchOnWeb(afterWord)) {
                convertedWordsPut(checkedWord, afterWord);
                return true;
            }
        }
        return false;
    }

    private boolean isKnownWord(String e) {
        if(dictionaries.hasSaved(e)){
            foundWords.add(e);
            return true;
        }
        return false;
    }
    private boolean isOnWeb(String e) {
        if (searchOnWeb(e)) {
            performanceMonitor.countOneSearch();
            foundWords.add(e);
            return true;
        }
        return false;
    }





    private boolean searchOnWeb(String word) {
        String url = "https://www.oxfordlearnersdictionaries.com/definition/english/" + word;
        try {
            (new java.net.URL(url)).openStream().close();
            return true;
        } catch (Exception ex) { }
        return false;
    }



    /**
     * Check whether a string has at least an upper case and return the locations of the uppercase.
     * Ignore capitalization at first letter and word are all upper cases.
     * @param word
     * @return
     */
    private static List<Integer> upperCaseLocation(String word) {

        List<Integer> allUpperCaseLocations = new ArrayList<>();
        for( int i = 1; i < word.length()-1; i++){
            if(Character.isUpperCase(word.charAt(i))){
                allUpperCaseLocations.add(i);
            }
        }
        if(allUpperCaseLocations.size() >= word.length()-2){
            return null;
        }
        if (allUpperCaseLocations.size() == 0) {
            return null;
        }
        return allUpperCaseLocations;
    }


    public void checkConvertedWords(Scanner scanner, String[] correctingWords){
        for (String word : correctingWords) {
            if(!convertedWords.containsKey(word)) throw new RuntimeException("wrong entered word\"" + word + "\"");
            System.out.println("Treat { " + word + " } as? enter correct word OR d for discard");
            String yn = scanner.nextLine().toLowerCase().replaceAll(" ","" );
            if (yn.equals("d")) {
                convertedWords.remove(word);
                continue;
            }
            confirmAnAasBPair(scanner, word, yn);
        }
        System.out.println("All converted words are checked!");
        System.out.println("Enter anything to continue ...");
        scanner.nextLine();
    }

    private void confirmAnAasBPair(Scanner scanner, String word, String yn) {
        System.out.println("Confirm the pair { " + word + ":" + yn + " }? y / other correct word" );
        String confirmPair = scanner.nextLine();
        if (confirmPair.equals("")) {
            convertedWords.replace(word, yn);
            return;
        }
        confirmAnAasBPair(scanner, word, confirmPair);
    }

    public void checkUnknownWords(Scanner scanner) throws SQLException, ClassNotFoundException {
        for(String unknownWord : unknownWords){
            System.out.println("Treat {" + unknownWord + "} as?  input word or / d (discard and ignore this word)");
            String input = scanner.nextLine().toLowerCase().replaceAll(" ", "");
            if (input.equals("d")) {
                toRemove.add(unknownWord);
                databaseOperator.uploadNewIgnoredWord(unknownWord);
                continue;
            }
            correctUnknownWord(scanner, unknownWord, input );
        }
        if (unknownWords.size() != toRemove.size()) {
            throw new RuntimeException("Something wrong! cannot clear unknown word list! Size Mismatch!");
        }
        unknownWords.removeAll(toRemove);
        toRemove.clear();
        System.out.println("All unknown words are operated!");
        System.out.println("Enter anything to continue");
        scanner.nextLine();
    }

    private void correctUnknownWord(Scanner scanner, String unknownWord, String input ) throws SQLException, ClassNotFoundException {
        System.out.println("Treat {" + unknownWord + "} as {" + input + "} ? y or / input new word or / d(discard and ignore this word)" );
        String input2 = scanner.nextLine();
        if (input2.equals("d")) {
            toRemove.add(unknownWord);
            databaseOperator.uploadNewIgnoredWord(unknownWord);
            return;
        }
        if ((input2.equals(""))) {
            databaseOperator.uploadNewAasB(unknownWord, input);
            toRemove.add(unknownWord);
            return;
        }
        correctUnknownWord(scanner, unknownWord, input2);
    }

    private void convertedWordsPut(String key, String value) {
        if (convertedWords.containsKey(key)) {
            return;
        }
        convertedWords.put(key, value);
    }

}
