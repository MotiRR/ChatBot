package sber.edu.session;

import sber.edu.session.info.SessionId;
import sber.edu.session.info.SessionInfo;
import sber.edu.webserver.responses.exceptions.BusinessLogicException;
import sber.edu.webserver.responses.exceptions.InternalServerErrorException;

import java.sql.SQLException;

public interface ISessionManager {
  SessionInfo createSession(int userId) throws SQLException;

  int getUserId(String login, String password) throws BusinessLogicException, SQLException;

  void dropSession(SessionId sessionId) throws SQLException, InternalServerErrorException;
}
