package sber.edu.database.dao;

import org.hibernate.Session;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import sber.edu.database.DataBaseManager;
import sber.edu.database.dto.MessagesEntity;
import sber.edu.database.dto.ResultAnalysisEntity;
import sber.edu.database.dto.SentencesEntity;
import sber.edu.webserver.responses.exceptions.InternalServerErrorException;

import java.sql.SQLException;
import java.util.List;

@Repository
public class SentencesDAO {

    public void createSentences(SentencesEntity sentencesEntity) throws SQLException {
        try (Session session = DataBaseManager.getInstance().getSession()) {
            session.beginTransaction();
            session.save(sentencesEntity);
            session.getTransaction().commit();
        }
    }

    public List<SentencesEntity> selectAll() throws SQLException {
        List sentencesList;
        try (Session session = DataBaseManager.getInstance().getSession()) {
            session.beginTransaction();
            String query = "select s from SentencesEntity s";
            sentencesList = session.createQuery(query)
                    .getResultList();
            session.getTransaction().commit();
        }
        return sentencesList;
    }

}
