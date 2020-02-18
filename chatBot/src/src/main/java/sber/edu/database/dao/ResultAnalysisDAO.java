package sber.edu.database.dao;

import org.hibernate.Session;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import sber.edu.database.DataBaseManager;
import sber.edu.database.dto.ResultAnalysisEntity;
import sber.edu.webserver.responses.exceptions.InternalServerErrorException;

import java.sql.SQLException;
import java.util.List;

@Repository
public class ResultAnalysisDAO {


    public void createResultAnalysis(ResultAnalysisEntity resultAnalysis) throws SQLException {
        try (Session session = DataBaseManager.getInstance().getSession()) {
            session.beginTransaction();
            session.persist(resultAnalysis);
            session.getTransaction().commit();
        }
    }


    public List<ResultAnalysisEntity> selectAllSentencesAndWordsUser(int userId) throws SQLException {
        List<ResultAnalysisEntity> resultAnalysisEntity;
        try (Session session = DataBaseManager.getInstance().getSession()) {
            session.beginTransaction();
            String query = "select s.sentence, ra.verb, ra.noun, ra.swear, ra.xxx, ra.spam, ra.secret " +
                    "from MessagesEntity m " +
                    "join m.sentences s " +
                    "join s.resultAnalysis ra " +
                    "where m.userId = :idUser";
            resultAnalysisEntity = session.createQuery(query)
                    .setParameter("idUser", userId)
                    .getResultList();
            session.getTransaction().commit();
        }
        return resultAnalysisEntity;
    }
}
