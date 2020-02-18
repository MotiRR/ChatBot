package sber.edu.session.info;

import org.junit.Test;

import java.util.Date;

import static org.junit.Assert.*;

public class SessionInfoTest {
  @Test
  public void testConstructor() {
    long currentTime = System.currentTimeMillis();
    SessionInfo info = new SessionInfo(new SessionId("12345"), 10, new Date(currentTime));

    assertEquals(new SessionId("12345"), info.getId());
    assertEquals(10, info.getUserId());
    assertEquals(new Date(currentTime), info.getCreationTimestamp());
    assertEquals(new Date(currentTime), info.getLastActivityTimestamp());
  }
}