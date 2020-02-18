package sber.edu.webserver.messenger.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public class ChatRequest {
  @JsonProperty("friendId")
  private int friendId;

  @JsonProperty("fromTime")
  private Long fromTime;

  public int getFriendId() {
    return friendId;
  }

  public Long getFromTime() {
    return fromTime;
  }
}
