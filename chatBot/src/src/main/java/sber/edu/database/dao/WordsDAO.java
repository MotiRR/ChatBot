package sber.edu.database.dao;

import org.hibernate.Session;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import sber.edu.database.DataBaseManager;
import sber.edu.database.dto.WordsEntity;
import sber.edu.kafka.restrictionSender.restrictions.RestrictionsIds;

import java.sql.SQLException;
import java.util.List;

@Repository
public class WordsDAO {

    public void createWord(WordsEntity WordEntity) throws SQLException {
        try (Session session = DataBaseManager.getInstance().getSession()) {
            session.beginTransaction();
            session.persist(WordEntity);
            session.getTransaction().commit();
        }
    }

    public List<String> selectWordWithRestrictionsIds(RestrictionsIds restrictionsIds) throws SQLException {
        List<String> wordsList;
        try (Session session = DataBaseManager.getInstance().getSession()) {
            session.beginTransaction();
            String query = "select w.word from WordsEntity w where w.status = :idRestrictions";
            wordsList = session.createQuery(query)
                    .setParameter("idRestrictions", restrictionsIds.toString())
                    .getResultList();
            session.getTransaction().commit();
        }
        return wordsList;
    }

    public static List<WordsEntity> selectWord() throws SQLException {
        List<WordsEntity> wordsList;
        try (Session session = DataBaseManager.getInstance().getSession()) {
            session.beginTransaction();
            String query = "select w.word from WordsEntity w";
            wordsList = session.createQuery(query)
                    .getResultList();
            session.getTransaction().commit();
        }
        return wordsList;
    }
}
