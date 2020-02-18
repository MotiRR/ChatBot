package sber.edu.session;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;
import sber.edu.session.info.SessionId;
import sber.edu.session.info.SessionInfo;
import sber.edu.webserver.responses.exceptions.BusinessLogicException;
import sber.edu.webserver.responses.exceptions.UnauthorizedException;

import java.sql.SQLException;
import java.util.Date;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class SessionKeeperTest {
  @Test
  public void createSession() throws SQLException, BusinessLogicException {
    ISessionManager sessionManager = Mockito.mock(ISessionManager.class);

    int userId = 777;
    SessionId expectedSessionId = new SessionId("session-token-Anton");
    Date date = new Date(System.currentTimeMillis());

    SessionInfo info = new SessionInfo(expectedSessionId, userId, date);

    when(sessionManager.getUserId("Anton", "pass")).thenReturn(userId);
    when(sessionManager.createSession(userId)).thenReturn(info);
    doNothing().when(sessionManager).dropSession(any(SessionId.class));

    SessionKeeper keeper = SessionKeeper.getInstance();
    keeper.setSessionManager(sessionManager);

    SessionId actualSessionId = keeper.createSession("Anton", "pass");
    assertEquals(expectedSessionId, actualSessionId);

    assertEquals(userId, keeper.getUserId(actualSessionId));
  }

  @Test
  public void getUserIdTest() {
    try {
      SessionKeeper.getInstance().getUserId(new SessionId("12345"));
      fail();
    } catch (UnauthorizedException ex) {
    } catch (Throwable ex) {
      fail();
    }
  }
}