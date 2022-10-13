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

public interface StringBlockOperator {

    int savedWordsNumber();
    int needCheckWordsNumber();
    int unknownWordsNumber();
    void printSavedWords();
    void printConvertedWords();
    void printUnknownWords();
    void uploadWords() throws SQLException, ClassNotFoundException ;
    void bunchIgnore(Scanner scanner, int[] indexes);
    void uploadCurrentAasBDictionary();
    void checkConvertedWords(Scanner scanner, String[] correctingWords);
    void checkUnknownWords(Scanner scanner) throws SQLException, ClassNotFoundException;


}
