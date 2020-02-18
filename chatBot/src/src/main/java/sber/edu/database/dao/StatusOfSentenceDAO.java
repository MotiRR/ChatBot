package sber.edu.database.dao;

import org.hibernate.Session;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import sber.edu.database.DataBaseManager;
import sber.edu.database.dto.SentencesEntity;
import sber.edu.database.dto.StatusOfSentenceEntity;
import sber.edu.webserver.responses.exceptions.InternalServerErrorException;

import java.sql.SQLException;

@Repository
public class StatusOfSentenceDAO {

    public void createStatusOfSentence(StatusOfSentenceEntity statusOfSentenceEntity)
            throws SQLException {
        try (Session session = DataBaseManager.getInstance().getSession()) {
            session.beginTransaction();
            session.persist(statusOfSentenceEntity);
            session.getTransaction().commit();
        }
    }



}
