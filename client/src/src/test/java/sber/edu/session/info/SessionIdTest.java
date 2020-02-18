package sber.edu.session.info;

import org.junit.Test;

import static org.junit.Assert.*;

public class SessionIdTest {

  @Test
  public void testToString() {
    SessionId session = new SessionId("Hello it's my SessionId");
    assertEquals("Hello it's my SessionId", session.toString());
  }

  @Test
  public void testEquals() {
    SessionId sessionLhs = new SessionId("Hello it's my SessionId");
    SessionId sessionRhs = new SessionId("Hello it's my SessionId");

    assertTrue(sessionLhs.equals(sessionLhs));
    assertTrue(sessionRhs.equals(sessionRhs));

    assertTrue(sessionLhs.equals(sessionRhs));
    assertTrue(sessionRhs.equals(sessionRhs));

    assertTrue(sessionLhs.hashCode() == sessionRhs.hashCode());
  }

  @Test
  public void testNotEquals() {
    SessionId sessionLhs = new SessionId("Hello it's my SessionId");
    SessionId sessionRhs = new SessionId("another SessionId");

    assertFalse(sessionLhs.equals(sessionRhs));
    assertFalse(sessionRhs.equals(sessionLhs));
  }
}