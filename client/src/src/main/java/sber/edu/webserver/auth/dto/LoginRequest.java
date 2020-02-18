package sber.edu.webserver.auth.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public class LoginRequest {
  @JsonProperty("login")
  public String login;

  @JsonProperty("password")
  public String password;
}