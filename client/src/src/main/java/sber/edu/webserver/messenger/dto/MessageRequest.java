package sber.edu.webserver.messenger.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public class MessageRequest {
  @JsonProperty("message")
  public String message;

  @JsonProperty("friendId")
  public Integer friendId;

  @JsonProperty("messageTime")
  public Long messageTime;
}