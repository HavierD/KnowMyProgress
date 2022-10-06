package tech.havier.databaseOperator;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;
import java.util.List;

public interface DatabaseOperator {

    HashMap<String, String> getTransitionDictionary();

    List<String> getWordDictionary();

    void addNewAasB(String key, String value);

    void uploadNewIgnoredWord(String word);

    List<String> getIgnoreDictionary();

    void updateWord(String word) throws SQLException, ClassNotFoundException ;

    void addNewWord(String word);

    void addNewRecord(String word);
}
