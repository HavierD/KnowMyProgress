package tech.havier.stringBlockOperator;



import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;

public interface StringBlockOperator {

    int savedWordsNumber();
    int needCheckWordsNumber();
    int unknownWordsNumber();

    void printSavedWords();
    void printConvertedWords();
    void printUnknownWords();

    void uploadFoundWords() ;
    void uploadCurrentAasBDictionary();


    void checkConvertedWords(Scanner scanner, String[] correctingWords);
    void checkUnknownWords(Scanner scanner) throws SQLException, ClassNotFoundException;







    default List<String> preProcess(String longStrBlock) {
        List<String> preProcessedStrings = parseLongStringIntoList(longStrBlock)
                .stream()
                .filter(e->!replaceWhiteSpace(e).equals(""))
                .collect(Collectors.toList());
        return preProcessedStrings;
    }
    private List<String> parseLongStringIntoList(String string) {
        String s1 = string
                .replaceAll("'s", " ")
                .replaceAll("\\p{Punct}", " ")
                .replaceAll("[0-9]", "");
        List<String> stringList = Arrays.asList(s1.split(" "));
        List<String> returnedList = splitCamelCaseWords(stringList);
        return returnedList;
    }

    private String replaceWhiteSpace(String string) {
        String string1 = string.replaceAll(" ", "");
        return string1;
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

    private List<Integer> upperCaseLocation(String word) {

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
