package sber.edu.services;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import sber.edu.database.dao.ResultAnalysisDAO;
import sber.edu.database.dto.ResultAnalysisEntity;
import sber.edu.kafka.messageReceiver.MessageReceiver;

import java.sql.SQLException;
import java.util.List;

@Service
public class ResultAnalysisService {

    public static final Logger logger = LogManager.getLogger(MessageReceiver.class);

    @Autowired
    ResultAnalysisDAO resultAnalysisDAO;

    public List<ResultAnalysisEntity> allSentencesAndWordsUser(int userId) {
        try {
            return resultAnalysisDAO.selectAllSentencesAndWordsUser(userId);
        } catch (SQLException e) {
            logger.info("exception all sentences and words of the user: " + e.getMessage());
            return null;
        }
    }
}
