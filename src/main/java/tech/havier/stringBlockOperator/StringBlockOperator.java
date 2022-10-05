package tech.havier.stringBlockOperator;


import tech.havier.Date;
import tech.havier.Dictionaries;
import tech.havier.databaseOperator.SqlOperator;
import tech.havier.stringBlockDelegate.StringBlockImporter;

import java.io.IOException;
import java.sql.SQLException;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class StringBlockOperator {

    private List<String> ignoredWords = new ArrayList<>();
    private List<String> toRemove = new ArrayList<>();
    private final List<String> foundWords = new ArrayList<>();
    private final ConcurrentHashMap<String, String> convertedWords = new ConcurrentHashMap<>();
    private final List<String> finalUnknownWords = new ArrayList<>();


    private Dictionaries dictionaries;
    private SqlOperator sqlOperator;

    private Date date;

    public StringBlockOperator(StringBlockImporter stringBlockImporter) throws Exception {
        dictionaries = new Dictionaries();
        sqlOperator = new SqlOperator();
        date = new Date();

        String string = stringBlockImporter.getStringBlock();
        List<String> wordsWithSpace = parseLongStringIntoList(string);
        List<String> wordsNoSpace = replaceBlankString(wordsWithSpace);
        List<String> transitionedWords = tryToTransition(wordsNoSpace);
        validateWords(transitionedWords);
    }

    public int savedWordsNumber() {return foundWords.size();}
    public int needCheckWordsNumber(){return convertedWords.size();}
    public int unknownWordsNumber(){return finalUnknownWords.size();}

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
        for (String e : finalUnknownWords) {
            System.out.println(index + ". " + e);
            index++;
        }
    }

    public void uploadWords() throws SQLException, ClassNotFoundException {
        for (String e : foundWords) {
            if (dictionaries.hasSaved(e)) {
                sqlOperator.updateWord(e);
                continue;
            }
            sqlOperator.addNewWord(e);
        }
        foundWords.clear();
        System.out.println("All found words upload successfully! Found-word-list has been cleared.");
    }

    private void uploadWord(String word) throws SQLException, ClassNotFoundException {
        if (dictionaries.hasSaved(word)) {
            sqlOperator.updateWord(word);
        }
        sqlOperator.addNewWord(word);
    }


    /**
     * replace all punctuation into space.
     * @param string
     * @return
     */
    private List<String> parseLongStringIntoList(String string) {
        String s1 = string
                .replaceAll("'s", " ").
                replaceAll("'", "QQQQ").
                replaceAll("\\p{Punct}", " ");
        List<String> stringList = Arrays.asList(s1.split(" "));
        List<String> returnedList = splitCamelCaseWords(stringList);
        return returnedList;
    }

    private static List<String> splitCamelCaseWords(List<String> stringList) {
        List<String> returnedList = new ArrayList<>();
        for (String e : stringList) {
            if(upperCaseLocation(e) == null){
                returnedList.add(e.toLowerCase());
            }
            else{
                returnedList.add(e.substring(0, upperCaseLocation(e).get(0)).toLowerCase());
                for (int i = 1; i < upperCaseLocation(e).size(); i++){
                    returnedList.add(e.substring(upperCaseLocation(e).get(i-1), upperCaseLocation(e).get(i)-1));
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
            if (dictionaries.transitions(e) != null) {
                returnedList.add(dictionaries.transitions(e));
                continue;
            }
            returnedList.add(e);
        }
        return returnedList;
    }


    private void validateWords(List<String> wordsCheckedAasB) throws IOException {
        for (String e : wordsCheckedAasB) {

            if(dictionaries.doesIgnore(e)){
                ignoredWords.add(e);
                continue;
            }

            if (dictionaries.hasSaved(e) || searchOnWeb(e)) {
                if(foundWords.contains(e)) continue;
                foundWords.add(e);
                continue;
            }
            if (e.endsWith("ies")) {
                String ed = e.substring(0, e.length() - 3) + "y";
                if (dictionaries.hasSaved(e) || searchOnWeb(ed)) {
                    convertedWords.put(e, ed);
                    continue;
                }
            }
            if (e.endsWith("ves")) {
                String ed = e.substring(0, e.length() - 3) + "f";
                if (dictionaries.hasSaved(e) || searchOnWeb(ed)) {
                    convertedWords.put(e, ed);
                    continue;
                }
            }
            if (e.endsWith("es")) {
                String ed = e.substring(0, e.length() - 2);
                if (dictionaries.hasSaved(e) || searchOnWeb(ed)) {
                    convertedWords.put(e, ed);
                    continue;
                }
            }
            if (e.endsWith("s")) {
                String ed = e.substring(0, e.length() - 1);
                if (dictionaries.hasSaved(e) || searchOnWeb(ed)) {
                    convertedWords.put(e, ed);
                    continue;
                }
            }
            if (e.endsWith("ying")) {
                String ed = e.substring(0, e.length() - 4) + "e";
                if (dictionaries.hasSaved(e) || searchOnWeb(ed)) {
                    convertedWords.put(e, ed);
                    continue;
                }
            }
            if (e.endsWith("ing")) {
                String ed = e.substring(0, e.length() - 3);
                if (dictionaries.hasSaved(e) || searchOnWeb(ed)) {
                    convertedWords.put(e, ed);
                    continue;
                } else {
                    if (dictionaries.hasSaved(e) || searchOnWeb(ed.substring(0, ed.length() - 1) + "e")) {
                        convertedWords.put(e, ed);
                        continue;
                    }
                }
            }
            if (e.endsWith("ied")) {
                String ed = e.substring(0, e.length() - 3) + "y";
                if (dictionaries.hasSaved(e) || searchOnWeb(ed)) {
                    convertedWords.put(e, ed);
                    continue;
                }
            }
            if (e.endsWith("ed")) {
                String ed = e.substring(0, e.length() - 2);
                if (dictionaries.hasSaved(e) || searchOnWeb(ed)) {
                    convertedWords.put(e, ed);
                    continue;
                } else {
                    ed = ed + "e";
                    if (dictionaries.hasSaved(e) || searchOnWeb(ed)) {
                        convertedWords.put(e, ed);
                        continue;
                    }
                }
            }
            finalUnknownWords.add(e);
        }
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
        if(word.contains("QQQQ")){
            return null;
        }

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


    public void checkConvertedWords(Scanner scanner, String[] correctingWords) throws SQLException, ClassNotFoundException {

        for (String word : correctingWords) {
            if (convertedWords.containsKey(word)) {
                System.out.println("Treat { " + word + " } as? enter correct word");
                String yn = scanner.nextLine();
                confirmAnAasBPair(scanner, word, yn);

            }
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

    private void correctPairs(Scanner scanner, Map.Entry<String, String> entry, String input) throws SQLException, ClassNotFoundException {
        System.out.println("Treat " + entry.getKey() + " as " + input + " ? y / d(discard and ignore this word) / correct word");
        String input2 = scanner.nextLine().toLowerCase().replaceAll(" ", "");
        if (input2.equals("")){
            sqlOperator.addNewAasB(entry.getKey(), input);
            uploadWord(input);
            convertedWords.remove(entry.getKey());
            return;
        }
        if(input2.equals("d")){
            convertedWords.remove(entry.getKey());
            sqlOperator.uploadNewIgnoredWord(entry.getKey());
            return;
        }
        correctPairs(scanner, entry, input2);
    }

    public void checkUnknownWords(Scanner scanner) throws SQLException, ClassNotFoundException {
        for(String unknownWord : finalUnknownWords){
            System.out.println("Treat " + unknownWord + " as?  input word or / d (discard and ignore this word)");
            String input = scanner.nextLine().toLowerCase().replaceAll(" ", "");
            if (input.equals("d")) {
                toRemove.add(unknownWord);
                sqlOperator.uploadNewIgnoredWord(unknownWord);
                continue;
            }
            correctUnknownWord(scanner, unknownWord, input );
        }
        if (finalUnknownWords.size() != toRemove.size()) {
            throw new RuntimeException("Something wrong! cannot clear unknown word list! Size Mismatch!");
        }
        finalUnknownWords.removeAll(toRemove);
        toRemove.clear();
        System.out.println("All unknown words are operated!");
        System.out.println("Enter anything to continue");
        scanner.nextLine();
    }

    private void correctUnknownWord(Scanner scanner, String unknownWord, String input ) throws SQLException, ClassNotFoundException {
        System.out.println("Treat " + unknownWord + " as " + input + " ? y or / input new word or / d(discard and ignore this word)" );
        String input2 = scanner.nextLine();
        if (input2.equals("d")) {
            toRemove.add(unknownWord);
            sqlOperator.uploadNewIgnoredWord(unknownWord);
            return;
        }
        if ((input2.equals(""))) {
            sqlOperator.addNewAasB(unknownWord, input);
            uploadWord(input);
            toRemove.add(unknownWord);
            return;
        }
        correctUnknownWord(scanner, unknownWord, input2);
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
                sqlOperator.uploadNewIgnoredWord(foundWords.get(e));
                toRemove.add(foundWords.get(e));
            }
            foundWords.removeAll(toRemove);
        }
    }

    public void uploadCurrentAasBDictionary() {
        for (Map.Entry<String, String> e : convertedWords.entrySet()){
            sqlOperator.addNewAasB(e.getKey(), e.getValue());
        }
        System.out.println("Current converted words uploaded successfully!");
        convertedWords.clear();
    }
}
