package tech.havier.databaseOperator.hibernate;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import tech.havier.databaseOperator.DatabaseOperator;
import tech.havier.databaseOperator.sql.SqlOperator;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
class HibernateOperatorTest {
    DatabaseOperator sql;
    DatabaseOperator hib;
    HibernateOperator hib2;

    @BeforeEach
    public void init() throws Exception {
        sql = SqlOperator.getSqlOperatorInstance();
        hib = new HibernateOperator();
        hib2 = new HibernateOperator();
    }

    @Test
    public void getDictionariesTest() {
        assertEquals(sql.getTransitionDictionary(),hib.getTransitionDictionary());
        assertEquals(sql.getWordDictionary(), hib.getWordDictionary());
        assertEquals(sql.getIgnoreDictionary(), hib.getIgnoreDictionary());
    }

    @Test
    public void uploadAasBTest() {
//        hib.uploadNewAasB("beforeword", "afterword");

//        HashMap<String, String> pairs = new HashMap<>();
//        pairs.put("bef1", "aft1");
//        pairs.put("bef2", "aft2");
//        hib.uploadNewAasBs(pairs);
    }

    @Test
    public void uploadIgnoredWordTest() {
//        hib.uploadNewIgnoredWord("ignoredone");
//        List<String> words = new ArrayList<>();
//        words.add("ign1");
//        words.add("ign2");
//        hib.uploadNewIgnoredWords(words);
    }

    @Test
    public void uploadNewWordTest() {
//        List<String> collection1 = new ArrayList<>();
//        collection1.add("ign1");
//        collection1.add("ign2");
//        HashMap<String, String> collection2 = new HashMap<>();
//        collection2.put("bef1", "aft1");
//        collection2.put("bef2", "aft2");
        List<String> list = new ArrayList<>();
        list.add("the");
        list.add("how");
        list.add("which");
        list.add("fsfs");
        hib.uploadNewWords(list);
    }

}