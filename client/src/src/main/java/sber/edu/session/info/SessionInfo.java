package sber.edu.session.info;

import java.util.Date;
import java.util.Objects;

public class SessionInfo {
  public SessionInfo(SessionId id, int userId, Date creationTimestamp) {
    this.id = id;
    this.userId = userId;
    this.creationTimestamp = creationTimestamp;
    this.lastActivityTimestamp = this.creationTimestamp;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    SessionInfo that = (SessionInfo) o;
    return Objects.equals(id, that.id) &&
      Objects.equals(userId, that.userId) &&
      Objects.equals(creationTimestamp, that.creationTimestamp) &&
      Objects.equals(lastActivityTimestamp, that.lastActivityTimestamp);
  }

  @Override
  public int hashCode() {
    return Objects.hash(id, userId, creationTimestamp, lastActivityTimestamp);
  }

  public int getUserId() {
    return userId;
  }

  public SessionId getId() {
    return id;
  }

  public Date getCreationTimestamp() {
    return creationTimestamp;
  }

  public Date getLastActivityTimestamp() {
    return lastActivityTimestamp;
  }

  public void setLastActivityTimestamp(Date lastActivityTimestamp) {
    this.lastActivityTimestamp = lastActivityTimestamp;
  }

  private SessionId id;
  private Integer userId;

  private Date creationTimestamp;
  private Date lastActivityTimestamp;
}
