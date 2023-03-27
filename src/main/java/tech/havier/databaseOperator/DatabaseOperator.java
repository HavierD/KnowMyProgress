package tech.havier.databaseOperator;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

public interface DatabaseOperator {

    HashMap<String, String> getTransitionDictionary();
    List<String> getWordDictionary();
    List<String> getIgnoreDictionary();

    void uploadNewAasB(String key, String value);
    void uploadNewAasBs(ConcurrentHashMap<String, String> pairs);

    void uploadNewIgnoredWord(String word);
    void uploadNewIgnoredWords(List<String> words);

    void updateWord(String word) throws SQLException, ClassNotFoundException ;

    void uploadNewWord(String word);
    void uploadNewWords(List<String> words);

    void uploadNewRecord(String word);
    void uploadNewRecords(List<String> words);

    List<String> getWordDictionaryByRepetitions();
}
