package sber.edu.session.info;

import java.util.Objects;

public class SessionId {
  public SessionId(String id) {
    this.id = id;
  }

  @Override
  public String toString() {
    return id;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    SessionId sessionId = (SessionId) o;
    return Objects.equals(id, sessionId.id);
  }

  @Override
  public int hashCode() {
    return Objects.hash(id);
  }

  private String id;
}
