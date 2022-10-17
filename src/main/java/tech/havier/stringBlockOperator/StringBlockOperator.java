package tech.havier.stringBlockOperator;



import java.sql.SQLException;
import java.util.Scanner;

public interface StringBlockOperator {

    int savedWordsNumber();
    int needCheckWordsNumber();
    int unknownWordsNumber();

    void printSavedWords();
    void printConvertedWords();
    void printUnknownWords();

    void uploadWords() throws SQLException, ClassNotFoundException;
    void uploadCurrentAasBDictionary();

    void bunchIgnore(Scanner scanner, int[] indexes);

    void checkConvertedWords(Scanner scanner, String[] correctingWords);
    void checkUnknownWords(Scanner scanner) throws SQLException, ClassNotFoundException;


}
