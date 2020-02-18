package sber.edu.webserver.auth.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public class RegisterRequest {
  @JsonProperty("login")
  public String login;

  @JsonProperty("password")
  public String password;

  @JsonProperty("passwordRetry")
  public String passwordRetry;

  @JsonProperty("name")
  public String name;

  @JsonProperty("secondName")
  public String secondName;

  @JsonProperty("lastName")
  public String lastName;

  @JsonProperty("phone")
  public String phone;

  @JsonProperty("email")
  public String email;
}
