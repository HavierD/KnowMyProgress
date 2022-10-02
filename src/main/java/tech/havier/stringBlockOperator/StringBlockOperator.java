package tech.havier.stringBlockOperator;


import tech.havier.userInterface.StringBlockImporter;

import java.io.IOException;
import java.util.*;

public class StringBlockOperator {




    private final Map<String, String> AasB = new HashMap<>();
    private final List<String> savedWords = new ArrayList<>();
    private final Map<String, String> convertedWords = new HashMap<>();
    private final List<String> finalUnknownWords = new ArrayList<>();


    public StringBlockOperator(StringBlockImporter stringBlockImporter) throws IOException {
        String string = stringBlockImporter.getStringBlock();
        List<String> wordsWithSpace = parseLongStringIntoList(string);
        List<String> wordsNoSpace = replaceBlankString(wordsWithSpace);
        List<String> wordsCheckedAasB = checkByAasB(wordsNoSpace);
        searchOnWeb(wordsCheckedAasB);
    }

    public int savedWordsNumber() {return savedWords.size();}
    public int needCheckWordsNumber(){return convertedWords.size();}
    public int unknownWordsNumber(){return finalUnknownWords.size();}

    public void printSavedWords() {
        int index = 0;
        for (String e : savedWords) {
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
        }
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

    private List<String> checkByAasB(List<String> list) {
        List<String> returnedList = new ArrayList<>();
        for (String e : list) {
            if (AasB.containsKey(e)) {
                returnedList.add(AasB.get(e));
                continue;
            }
            returnedList.add(e);
        }
        return returnedList;
    }


    private void searchOnWeb(List<String> wordsCheckedAasB) throws IOException {
        String searchQuery = "like";
        for (String e : wordsCheckedAasB) {
            if (searchOnWeb(e)) {
                if(savedWords.contains(e)) continue;
                savedWords.add(e);
                continue;
            }
            if (e.endsWith("ies")) {
                String ed = e.substring(0, e.length() - 3) + "y";
                if (searchOnWeb(ed)) {
                    convertedWords.put(e, ed);
                    continue;
                }
            }
            if (e.endsWith("ves")) {
                String ed = e.substring(0, e.length() - 3) + "f";
                if (searchOnWeb(ed)) {
                    convertedWords.put(e, ed);
                    continue;
                }
            }
            if (e.endsWith("es")) {
                String ed = e.substring(0, e.length() - 2);
                if (searchOnWeb(ed)) {
                    convertedWords.put(e, ed);
                    continue;
                }
            }
            if (e.endsWith("s")) {
                String ed = e.substring(0, e.length() - 1);
                if (searchOnWeb(ed)) {
                    convertedWords.put(e, ed);
                    continue;
                }
            }
            if (e.endsWith("ying")) {
                String ed = e.substring(0, e.length() - 4) + "e";
                if (searchOnWeb(ed)) {
                    convertedWords.put(e, ed);
                    continue;
                }
            }
            if (e.endsWith("ing")) {
                String ed = e.substring(0, e.length() - 3);
                if (searchOnWeb(ed)) {
                    convertedWords.put(e, ed);
                    continue;
                } else {
                    if (searchOnWeb(ed.substring(0, ed.length() - 1) + "e")) {
                        convertedWords.put(e, ed);
                        continue;
                    }
                }
            }
            if (e.endsWith("ied")) {
                String ed = e.substring(0, e.length() - 3) + "y";
                if (searchOnWeb(ed)) {
                    convertedWords.put(e, ed);
                    continue;
                }
            }
            if (e.endsWith("ed")) {
                String ed = e.substring(0, e.length() - 2);
                if (searchOnWeb(ed)) {
                    convertedWords.put(e, ed);
                    continue;
                } else {
                    ed = ed + "e";
                    if (searchOnWeb(ed)) {
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



}
