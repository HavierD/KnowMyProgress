package tech.havier.stringBlockOperator;

import tech.havier.Dictionaries;
import tech.havier.databaseOperator.DatabaseFactory;
import tech.havier.stringBlockDelegate.StringBlockImporter;

import java.sql.SQLException;
import java.util.List;
import java.util.Scanner;

public class StringBlockOperator2 implements StringBlockOperator{

    public StringBlockOperator2(StringBlockImporter stringBlockImporter) throws Exception {

//        this.dictionaries = new Dictionaries();
//        this.databaseOperator = new DatabaseFactory().createDatabaseOperator();
//
//        String string = stringBlockImporter.getStringBlock();
//        List<String> wordsWithSpace = parseLongStringIntoList(string);
//        List<String> wordsNoSpace = replaceBlankString(wordsWithSpace);
//        List<String> transitionedWords = tryToTransition(wordsNoSpace);
//        validateWords(transitionedWords);
//        performanceMonitor.proportionOfSearchingOnline();
//        finalChecking();
    }

    @Override
    public int savedWordsNumber() {
        return 0;
    }

    @Override
    public int needCheckWordsNumber() {
        return 0;
    }

    @Override
    public int unknownWordsNumber() {
        return 0;
    }

    @Override
    public void printSavedWords() {

    }

    @Override
    public void printConvertedWords() {

    }

    @Override
    public void printUnknownWords() {

    }

    @Override
    public void uploadWords() throws SQLException, ClassNotFoundException {

    }

    @Override
    public void bunchIgnore(Scanner scanner, int[] indexes) {

    }

    @Override
    public void uploadCurrentAasBDictionary() {

    }

    @Override
    public void checkConvertedWords(Scanner scanner, String[] correctingWords) {

    }

    @Override
    public void checkUnknownWords(Scanner scanner) throws SQLException, ClassNotFoundException {

    }
}
