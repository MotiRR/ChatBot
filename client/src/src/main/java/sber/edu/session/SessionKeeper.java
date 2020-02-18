package sber.edu.session;

import sber.edu.session.info.SessionId;
import sber.edu.session.info.SessionInfo;
import sber.edu.webserver.responses.exceptions.BusinessLogicException;
import sber.edu.webserver.responses.exceptions.InternalServerErrorException;
import sber.edu.webserver.responses.exceptions.UnauthorizedException;

import java.sql.SQLException;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class SessionKeeper {
  public static SessionKeeper getInstance() {
    if (instance == null) {
      synchronized (SessionKeeper.class) {
        if (instance == null) {
          instance = new SessionKeeper();
        }
      }
    }
    return instance;
  }

  private static SessionKeeper instance;

  private SessionKeeper() {
    this.sessions = Collections.synchronizedMap(new HashMap<SessionId, SessionInfo>());
  }

  public SessionId createSession(String login, String password) throws SQLException, BusinessLogicException {
    int userId = sessionManager.getUserId(login, password);
    SessionInfo session = findSessionByUserId(userId);

    if (session == null) {
      session = sessionManager.createSession(userId);
      sessions.put(session.getId(), session);
    }

    return session.getId();
  }

  public int getUserId(SessionId sessionId) throws UnauthorizedException {
    SessionInfo info = sessions.get(sessionId);
    if (info == null) {
      validateSession(sessionId);
    }
    return info.getUserId();
  }

  public boolean validateSession(SessionId session) throws UnauthorizedException {
    if (!sessions.containsKey(session)) {
      throw new UnauthorizedException();
    }
    return true;
  }

  public void dropSession(SessionId session) throws SQLException, InternalServerErrorException {
    sessions.remove(session);
    sessionManager.dropSession(session);
  }

  private SessionInfo findSessionByUserId(int userId) {
    synchronized (sessions) {
      for (SessionInfo info : sessions.values()) {
        if (info.getUserId() == userId) {
          return info;
        }
      }
    }
    return null;
  }

  private ISessionManager sessionManager;
  private Map<SessionId, SessionInfo> sessions;

  public void setSessionManager(ISessionManager manager) {
    this.sessionManager = manager;
  }
}
