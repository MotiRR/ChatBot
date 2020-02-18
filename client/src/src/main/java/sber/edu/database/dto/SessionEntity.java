package sber.edu.database.dto;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "session", schema = "main")
public class SessionEntity {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Integer id;

  public String getSessionId() {
    return sessionId;
  }

  public void setSessionId(String sessionId) {
    this.sessionId = sessionId;
  }

  @Basic
  @Column(name = "sessionId")
  private String sessionId;

  public boolean isClosed() {
    return isClosed;
  }

  public void setClosed(boolean closed) {
    isClosed = closed;
  }

  @Basic
  @Column(name = "isClosed")
  private boolean isClosed;

  public Integer getUserId() {
    return userId;
  }

  public void setUserId(Integer userId) {
    this.userId = userId;
  }

  @Basic
  @Column(name = "userId")
  private Integer userId;

  public Date getClosedTimestamp() {
    return closedTimestamp;
  }

  public void setClosedTimestamp(Date closedTimestamp) {
    this.closedTimestamp = closedTimestamp;
  }

  @Basic
  @Column(name = "closedTimestamp")
  private java.util.Date closedTimestamp;

  @Basic
  @Column(name = "creationTimestamp")
  private java.util.Date creationTimestamp;

  public Date getCreationTimestamp() {
    return creationTimestamp;
  }

  public void setCreationTimestamp(Date creationTimestamp) {
    this.creationTimestamp = creationTimestamp;
  }

  public void setId(Integer id) {
    this.id = id;
  }

  public Integer getId() {
    return id;
  }
}
