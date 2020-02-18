package sber.edu.webserver.profile.dto;

import lombok.Data;

import java.util.List;

@Data
public class UserProfile {
  private String photo;
  private String name;
  private String lastName;
  private String secondName;
  private short age;
  private String phone;
  private String email;

  private boolean isBlocked;
  private List<String> blockingDescription;
}
