package sber.edu.restrictions;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.hibernate.Session;
import sber.edu.database.DataBaseManager;
import sber.edu.kafka.messageReceiver.MessageListener;
import sber.edu.kafka.messageReceiver.RestrictionEntity;
import sber.edu.kafka.messageReceiver.RestrictionsTypes;

import java.sql.SQLException;
import java.util.stream.Collectors;

public class RestrictionsSaver implements MessageListener {
  public static final Logger logger = LogManager.getLogger(RestrictionsSaver.class);

  @Override
  public void messageReceived(RestrictionEntity restriction) {
    try {
      if (RestrictionsTypes.LOCK == restriction.getType()) {
        processUserLocking(restriction.getUserId(), restriction.getDescription().stream().collect(Collectors.joining(";")));
      } else {
        processUserUnlocking(restriction.getUserId());
      }
    } catch (Exception ex) {
      logger.error("Received exception during restriction processing: " + ex.getStackTrace());
    }
  }

  private void processUserUnlocking(Integer userId) throws SQLException {
    try (Session session = DataBaseManager.getInstance().getSession()) {
      session.beginTransaction();
      session.createQuery("UPDATE UserEntity u SET u.isBlocked = 0, u.blockingDescription = NULL WHERE u.id = :id")
        .setParameter("id", userId)
        .executeUpdate();
      session.getTransaction().commit();
    }
  }

  private void processUserLocking(Integer userId, String blockingDescription) throws SQLException {
    try (Session session = DataBaseManager.getInstance().getSession()) {
      session.beginTransaction();
      session.createQuery("UPDATE UserEntity u SET u.isBlocked = 1, u.blockingDescription = :desc WHERE u.id = :id")
        .setParameter("id", userId)
        .setParameter("desc", blockingDescription)
        .executeUpdate();
      session.getTransaction().commit();
    }
  }
}
