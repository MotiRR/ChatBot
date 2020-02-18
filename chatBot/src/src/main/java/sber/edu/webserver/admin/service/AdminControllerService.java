package sber.edu.webserver.admin.service;

import com.google.gson.Gson;
import org.hibernate.Session;
import sber.edu.database.DataBaseManager;
import sber.edu.database.dto.MessagesEntity;
import sber.edu.database.dto.ResultAnalysisEntity;
import sber.edu.database.dto.SentencesEntity;
import sber.edu.kafka.restrictionSender.restrictions.Restrictions;
import sber.edu.kafka.restrictionSender.restrictions.RestrictionsIds;
import sber.edu.webserver.admin.dto.MessageInfo;
import sber.edu.webserver.admin.dto.SentenceInfo;

import java.sql.SQLException;
import java.util.*;
import java.util.stream.Collectors;

public class AdminControllerService {
  public static List<MessageInfo> getUserMessages(Integer userId) throws SQLException {
    List<MessageInfo> messages = null;
    try (Session session = DataBaseManager.getInstance().getSession()) {
      session.beginTransaction();
      List<MessagesEntity> entities = session.createQuery(
        "FROM MessagesEntity m where m.userId = :id ORDER BY m.dateOfSend"
      ).setParameter("id", userId).list();

      messages = entities.stream().map(
        (m) -> {
          List<SentenceInfo> sentences = getSentences(m);
          return new MessageInfo(m.getId(), m.getDateOfSend(), m.getMessage(), sentences);
        }
      ).collect(Collectors.toList());

      session.getTransaction().commit();
    }
    return messages;
  }

  private static List<SentenceInfo> getSentences(MessagesEntity m) {
    return m.getSentences().stream().map(
      (s) -> {
        List<String> restrictions = getRestrictions(s);

        List<String> verbs = getVerbs(s);
        List<String> nouns = getNouns(s);

        String highlightedSentence = getHighlightedSentence(s);

        return new SentenceInfo(s.getId(), highlightedSentence, nouns, verbs, restrictions);

      }
    ).collect(Collectors.toList());
  }

  private static List<String> getVerbs(SentencesEntity s) {
    ResultAnalysisEntity analysisList = s.getResultAnalysis();
    Gson gson = new Gson();
    return gson.fromJson(analysisList.getVerb(), List.class);
  }

  private static List<String> getNouns(SentencesEntity s) {
    ResultAnalysisEntity analysisList = s.getResultAnalysis();
    Gson gson = new Gson();
    return gson.fromJson(analysisList.getNoun(), List.class);
  }


  private static List<String> getRestrictions(SentencesEntity s) {
    Set<String> restrictions = s.getStatusOfSentence().stream().map(
      (r) -> {
        RestrictionsIds id = RestrictionsIds.valueOf(r.getStatus());
        return Restrictions.getTranslation(id);
      }
    ).collect(Collectors.toSet());

    return new ArrayList<>(restrictions);
  }

  private static String getHighlightedSentence(SentencesEntity s) {
    String sentence = s.getSentence();

    ResultAnalysisEntity analysisList = s.getResultAnalysis();

    Gson gson = new Gson();

    Set<String> swearWords = new HashSet((List<String>) gson.fromJson(analysisList.getSwear(), List.class));
    for (String wordToHighlight : swearWords) {
      sentence = sentence.replaceAll(wordToHighlight, "<b>" + wordToHighlight + "</b>");
    }

    Set<String> secretWords = new HashSet((List<String>) gson.fromJson(analysisList.getSecret(), List.class));
    for (String wordToHighlight : secretWords) {
      sentence = sentence.replaceAll(wordToHighlight, "<b>" + wordToHighlight + "</b>");
    }

    Set<String> spamWords = new HashSet((List<String>) gson.fromJson(analysisList.getSpam(), List.class));
    for (String wordToHighlight : spamWords) {
      sentence = sentence.replaceAll(wordToHighlight, "<b>" + wordToHighlight + "</b>");
    }

    Set<String> pornoWords = new HashSet((List<String>) gson.fromJson(analysisList.getXxx(), List.class));
    for (String wordToHighlight : pornoWords) {
      sentence = sentence.replaceAll(wordToHighlight, "<b>" + wordToHighlight + "</b>");
    }

    return sentence;
  }
}
