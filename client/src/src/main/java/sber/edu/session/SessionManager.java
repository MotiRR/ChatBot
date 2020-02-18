package sber.edu.session;

import org.hibernate.Session;
import sber.edu.database.DataBaseManager;
import sber.edu.database.dto.SessionEntity;
import sber.edu.database.dto.UserAccountEntity;
import sber.edu.session.info.SessionId;
import sber.edu.session.info.SessionInfo;
import sber.edu.webserver.responses.exceptions.BusinessLogicException;
import sber.edu.webserver.responses.exceptions.InternalServerErrorException;

import java.sql.SQLException;
import java.util.Date;
import java.util.List;
import java.util.UUID;

public class SessionManager implements ISessionManager {
  public SessionManager() {
    closeAllSessions();
  }

  private void closeAllSessions() {
    try (Session session = DataBaseManager.getInstance().getSession()) {
      session.beginTransaction();

      String script = "FROM SessionEntity session WHERE session.isClosed = false";
      List<SessionEntity> sessions = session.createQuery(script).list();

      Date currentDate = new Date(System.currentTimeMillis());
      sessions.stream().forEach((SessionEntity info) -> {
        info.setClosed(true);
        info.setClosedTimestamp(currentDate);
        session.save(info);
      });

      session.getTransaction().commit();
    } catch (SQLException e) {
      e.printStackTrace();
    }
  }

  public SessionInfo createSession(int userId) throws SQLException {
    SessionInfo info = null;

    info = new SessionInfo(
      new SessionId(
        UUID.randomUUID().toString()),
      userId,
      new Date(System.currentTimeMillis())
    );

    try (Session session = DataBaseManager.getInstance().getSession()) {
      session.beginTransaction();

      SessionEntity entity = new SessionEntity();
      entity.setUserId(info.getUserId());
      entity.setSessionId(info.getId().toString());
      entity.setClosed(false);
      entity.setCreationTimestamp(info.getCreationTimestamp());

      session.save(entity);

      session.getTransaction().commit();
    }

    return info;
  }

  public void dropSession(SessionId sessionId) throws SQLException, InternalServerErrorException {
    try (Session session = DataBaseManager.getInstance().getSession()) {
      session.beginTransaction();

      String script = "FROM SessionEntity session WHERE session.sessionId = :id AND session.isClosed = false";
      List<SessionEntity> sessions = session.createQuery(script).setParameter("id", sessionId.toString()).list();

      if (sessions.isEmpty())
      {
        return;
      }

      if (sessions.size() > 1) {
        throw new InternalServerErrorException();
      }

      SessionEntity info = sessions.get(0);
      info.setClosed(true);
      info.setClosedTimestamp(new Date(System.currentTimeMillis()));

      session.save(info);
      session.getTransaction().commit();
    }
  }

  public int getUserId(String login, String password) throws BusinessLogicException, SQLException {
    Integer userId = null;
    try (Session session = DataBaseManager.getInstance().getSession()) {
      String script = "FROM UserAccountEntity account WHERE account.login = :login AND account.password = :password";
      List<UserAccountEntity> accounts = session.createQuery(script)
        .setParameter("login", login)
        .setParameter("password", password).list();

      if (accounts.size() > 1) {
        throw new InternalServerErrorException();
      }

      if (accounts.size() == 0) {
        throw new BusinessLogicException("Пользователь с таким логином и/или паролем не существует");
      }

      userId = accounts.get(0).getId();
    }
    return userId;
  }
}
