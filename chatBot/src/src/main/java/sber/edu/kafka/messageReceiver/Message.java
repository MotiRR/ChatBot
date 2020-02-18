package sber.edu.kafka.messageReceiver;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Message {
  public Message() {
  }

  public Message(int fromUser, int toUser, MessageTypes type, String message, Long time) {
    this.fromUser = fromUser;
    this.toUser = toUser;
    this.message = message;
    this.type = type;
    this.time = time;
  }

  public int getFromUser() {
    return fromUser;
  }

  public void setFromUser(int fromUser) {
    this.fromUser = fromUser;
  }

  public int getToUser() {
    return toUser;
  }

  public void setToUser(int toUser) {
    this.toUser = toUser;
  }

  public String getMessage() {
    return message;
  }

  public void setMessage(String message) {
    this.message = message;
  }

  public Long getTime() {
    return time;
  }

  public void setTime(Long time) {
    this.time = time;
  }

  public MessageTypes getType() {
    return type;
  }

  public void setType(MessageTypes type) {
    this.type = type;
  }

  @JsonProperty("type")
  private MessageTypes type;

  @JsonProperty("fromUser")
  private int fromUser;

  @JsonProperty("toUser")
  private int toUser;

  @JsonProperty("message")
  private String message;

  @JsonProperty("time")
  private Long time;

  @Override
  public String toString() {
    return "Message at " + time + '\n' +
      "[from id = " + fromUser + ']' + '\n' +
      "[to id = " + toUser + ']' + '\n' +
      "type: " + type.toString() + '\n' +
      "content: \'" + message + "\'\n";
  }
}
