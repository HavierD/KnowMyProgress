package tech.havier.databaseOperator.hibernate;

import org.hibernate.Session;
import tech.havier.databaseOperator.DatabaseOperator;
import tech.havier.databaseOperator.hibernate.entities.*;
import tech.havier.timeToolkit.HavierDate;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import java.sql.SQLException;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class HibernateOperator implements DatabaseOperator {

    private HavierDate havierDate = new HavierDate();
    private List<WordsList> wordsDictionary;
    static EntityManagerFactory ef = Persistence.createEntityManagerFactory("knowMyProgress");

    public HibernateOperator() {
        initializeWordDic();
    }



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
        HashMap<String, String> transDic = new HashMap<>();
        EntityManager em = ef.createEntityManager();
        em.getTransaction().begin();
        List<AasB> transDicList = em.createQuery("select a from AasB a", AasB.class).getResultList();
        transDicList.forEach(w -> transDic.put(w.getBeforeWord(), w.getAfterWord()));
        return transDic;
    }

    @Override
    public List<String> getWordDictionary() {
        List<String> wordDic = new ArrayList<>();
        wordsDictionary.forEach(w -> wordDic.add(w.getWord()));
        return wordDic;
    }

    @Override
    public List<String> getIgnoreDictionary() {
        List<String> ignDic = new ArrayList<>();
        EntityManager em = ef.createEntityManager();
        em.getTransaction().begin();
        List<IgnoredWord> ignList = em.createQuery("select i from IgnoredWord i order by word", IgnoredWord.class).getResultList();
        ignList.forEach(w -> ignDic.add(w.getWord()));
        return ignDic;
    }

    @Override
    public void uploadNewAasB(String key, String value) {
        EntityManager em = ef.createEntityManager();
        em.getTransaction().begin();
        AasB pair = new AasB(key, value);
        em.persist(pair);
        em.getTransaction().commit();
        em.close();
    }

    @Override
    public void uploadNewAasBs(ConcurrentHashMap<String, String> pairs) {
        EntityManager em = ef.createEntityManager();
        em.getTransaction().begin();
        for (Map.Entry<String, String> set : pairs.entrySet()) {
            AasB pair = new AasB(set.getKey(), set.getValue());
            em.persist(pair);
        }
        em.getTransaction().commit();
        em.close();
    }

    @Override
    public void uploadNewIgnoredWord(String word) {
        EntityManager em = ef.createEntityManager();
        em.getTransaction().begin();
        IgnoredWord ignoredWord = new IgnoredWord(word);
        em.persist(ignoredWord);
        em.getTransaction().commit();
        em.close();
    }

    @Override
    public void uploadNewIgnoredWords(List<String> words) {
        EntityManager em = ef.createEntityManager();
        em.getTransaction().begin();
        for (String word : words) {
            IgnoredWord ignoredWord = new IgnoredWord(word);
            em.persist(ignoredWord);
        }
        em.getTransaction().commit();
        em.close();
    }


    @Override
    public void updateWord(String word) throws SQLException, ClassNotFoundException {
    }

    @Deprecated
    @Override
    public void uploadNewWord(String word) {
        throw new RuntimeException("Don't use uploadNewWord() for hibernate. Use uploadNewWords() instead.");

    }

    @Override
    public void uploadNewWords(List<String> words) {
        EntityManager em = ef.createEntityManager();
        Session session = (Session) em.getDelegate();
        session.beginTransaction();
        words.forEach(e -> {
            String query = String.format("" +
                            "merge into words_list dest " +
                            "using (select '%1$s' word from dual) src " +
                            "on (dest.word = src.word) " +
                            "when matched then update set repetition = repetition + 1, last_meet_time = '%2$s' " +
                            "when not matched then insert(word, repetition, last_meet_time) values (src.word, 1, '%2$s')",
                    e, havierDate.getCurrentWholeDate());
            session.createSQLQuery(query).executeUpdate();
        });
        session.getTransaction().commit();
        session.close();
    }

    @Override
    public void uploadNewRecord(String word) {
        EntityManager em = ef.createEntityManager();
        em.getTransaction().begin();
        WordReadingRecords record = new WordReadingRecords(word, havierDate.getCurrentYear(),
                havierDate.getCurrentMonth(),
                havierDate.getCurrentDate());
        em.persist(record);
        em.getTransaction().commit();
        em.close();
    }

    @Override
    public void uploadNewRecords(List<String> words) {
        EntityManager em = ef.createEntityManager();
        em.getTransaction().begin();
        words.forEach(e ->{
            WordReadingRecords record = new WordReadingRecords(e, havierDate.getCurrentYear(),
                    havierDate.getCurrentMonth(),
                    havierDate.getCurrentDate());
            em.persist(record);
        });
        em.getTransaction().commit();
        em.close();
    }

    private void initializeWordDic() {
        EntityManager em = ef.createEntityManager();
        em.getTransaction().begin();
        wordsDictionary = em.createQuery("select w from WordsList w order by word", WordsList.class).getResultList();
        em.close();
    }
}
