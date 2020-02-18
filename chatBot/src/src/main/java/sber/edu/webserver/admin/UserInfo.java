package sber.edu.webserver.admin;

import lombok.Data;

@Data
public class UserInfo {
  private Integer id;
  private String name;
  private boolean isLocked;
  private String description;

  public UserInfo(Integer id, String name, boolean isLocked, String description) {
    this.id = id;
    this.name = name;
    this.isLocked = isLocked;
    this.description = description;
  }
}
