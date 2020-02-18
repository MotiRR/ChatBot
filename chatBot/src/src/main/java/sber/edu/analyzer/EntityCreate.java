package sber.edu.analyzer;

import com.google.gson.Gson;
import org.springframework.stereotype.Service;
import sber.edu.database.dao.MessagesDAO;
import sber.edu.database.dto.*;
import sber.edu.kafka.messageReceiver.Message;
import sber.edu.kafka.restrictionSender.RestrictionEntity;
import sber.edu.kafka.restrictionSender.restrictions.RestrictionsIds;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@Service
public class EntityCreate {
    private MessagesDAO messagesDAO = new MessagesDAO();
    private Message message;
    private MessagesEntity messagesEntity;
    private SentencesEntity sentencesEntity;
    private ResultAnalysisEntity resultAnalysisEntity;
    private StatusOfSentenceEntity statusOfSentenceEntity;
    private UserLockEntity userLockEntity;

    public EntityCreate() {
    }

    public EntityCreate(Message message) {
        this.message = message;
    }

    public void entityCreateMessage() {
        messagesEntity = new MessagesEntity();
        messagesEntity.setMessage(message.getMessage());
        messagesEntity.setUserId(message.getFromUser());
        messagesEntity.setDateOfSend(message.getTime());
    }

    public void entityCreateSentence(String sentence) {
        sentencesEntity = new SentencesEntity();
        sentencesEntity.setSentence(sentence);
        sentencesEntity.setMessage(messagesEntity);
    }

    public void entityCreateResultAnalysis(ArrayList<List<String>> allWords) {
        resultAnalysisEntity = new ResultAnalysisEntity();
        resultAnalysisEntity.setSentence(sentencesEntity);
        /*индексы
        0 - nounPOC;
        1 - verbPOC;
        2 - swear;
        3 - xxx
        4 - spam
        5 - secret
        */

        Gson gson = new Gson();

        resultAnalysisEntity.setNoun(gson.toJson(allWords.get(0)));
        resultAnalysisEntity.setVerb(gson.toJson(allWords.get(1)));
        resultAnalysisEntity.setSwear(gson.toJson(allWords.get(2)));
        resultAnalysisEntity.setXxx(gson.toJson(allWords.get(3)));
        resultAnalysisEntity.setSpam(gson.toJson(allWords.get(4)));
        resultAnalysisEntity.setSecret(gson.toJson(allWords.get(5)));
    }

    public ResultAnalysisEntity getResultAnalysisEntity() {
        return resultAnalysisEntity;
    }

    public void entityCreateStatusOfSentence(RestrictionsIds status) {
        statusOfSentenceEntity = new StatusOfSentenceEntity();
        statusOfSentenceEntity.setSentence(sentencesEntity);
        statusOfSentenceEntity.setStatus(status.toString());
    }

    public void addSentenceToMessage() {
        messagesEntity.addSentences(sentencesEntity);
    }

    public void addResultAnalysisToSentence() {
        sentencesEntity.addResultAnalysis(resultAnalysisEntity);
    }

    public void addStatusOfSentence() {
        sentencesEntity.addStatusOfSentence(statusOfSentenceEntity);
    }

    public UserLockEntity createUserLockEntity(RestrictionEntity restrictionEntity, int dayBlock) {
        userLockEntity = new UserLockEntity();
        userLockEntity.setIdUser(restrictionEntity.getUserId());
        userLockEntity.setDateOfLock(System.currentTimeMillis());
        userLockEntity.setDateOfUnlock(System.currentTimeMillis() + (dayBlock * 86400 * 1000)); //60000 = 1 минуте
        userLockEntity.setTypeOfRestriction(restrictionEntity.getType().toString());
        userLockEntity.setReason(restrictionEntity.getDescription().toString());
        return userLockEntity;
    }

    public void saveEntity() throws SQLException {
        messagesDAO.createMessage(messagesEntity);
    }

}
