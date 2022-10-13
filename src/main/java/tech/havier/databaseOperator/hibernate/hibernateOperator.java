package tech.havier.databaseOperator.hibernate;

import tech.havier.databaseOperator.DatabaseOperator;
import tech.havier.databaseOperator.hibernate.entities.AasB;
import tech.havier.databaseOperator.hibernate.entities.Test1;
import tech.havier.databaseOperator.hibernate.entities.WordReadingRecords;
import tech.havier.databaseOperator.hibernate.entities.WordsList;
import tech.havier.timeToolkit.HavierDate;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

public class hibernateOperator implements DatabaseOperator {

    HavierDate havierDate = new HavierDate();
    static EntityManagerFactory ef = Persistence.createEntityManagerFactory("knowMyProgress");

    public static void main(String[] args) {


//        EntityManagerFactory entityManagerFactory = Persistence.createEntityManagerFactory("knowMyProgress");
//        EntityManager em = entityManagerFactory.createEntityManager();
//        em.getTransaction().begin();
////        Test1 t1 = em.find(Test1.class,1);
//        Test1 t1 = new Test1(22, "qweqq", 4);
//
////        em.merge(t1);
//        em.persist(t1);
//        em.getTransaction().commit();
//
//        em.close();


//        EntityManagerFactory entityManagerFactory = Persistence.createEntityManagerFactory("knowMyProgress");
//        EntityManager em = entityManagerFactory.createEntityManager();
//        em.getTransaction().begin();
//        WordReadingRecords w1 = new WordReadingRecords("qqqq", "333", "444", "555");
//        em.persist(w1);
//        em.getTransaction().commit();
//        em.close();
    }


    @Override
    public HashMap<String, String> getTransitionDictionary() {
        HashMap<String, String >transDic = new HashMap<>();
        EntityManager em = ef.createEntityManager();
        em.getTransaction().begin();
        List<AasB> transDicList = em.createQuery("select a from AasB a", AasB.class).getResultList();
        transDicList.forEach(w->transDic.put(w.getBeforeWord(),w.getAfterWord()));
        return transDic;
    }

    @Override
    public List<String> getWordDictionary() {
        List<String> wordDic = new ArrayList<>();
        EntityManager em = ef.createEntityManager();
        em.getTransaction().begin();
        List<WordsList> wordDicList = em.createQuery("select w from WordsList w", WordsList.class).getResultList();
        wordDicList.forEach(w -> {
            wordDic.add(w.getWord());
        });
        return wordDic;
    }

    @Override
    public void addNewAasB(String key, String value) {
        EntityManager em = ef.createEntityManager();
        em.getTransaction().begin();
        AasB pair = new AasB(key, value);
        WordReadingRecords w1 = new WordReadingRecords("qqqq", "333", "444", "555");
        em.persist(w1);
        em.getTransaction().commit();
        em.close();
    }

    @Override
    public void uploadNewIgnoredWord(String word) {

    }

    @Override
    public List<String> getIgnoreDictionary() {
        return null;
    }

    @Override
    public void updateWord(String word) throws SQLException, ClassNotFoundException {

    }

    @Override
    public void addNewWord(String word) {

    }

    @Override
    public void addNewRecord(String word) {

    }
}
