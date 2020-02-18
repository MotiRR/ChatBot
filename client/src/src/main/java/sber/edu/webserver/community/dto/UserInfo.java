package sber.edu.webserver.community.dto;

public class UserInfo {
  public UserInfo(Integer id, String name) {
    this.name = name;
    this.id = id;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public Integer getId() {
    return id;
  }

  public void setId(Integer id) {
    this.id = id;
  }

  public String getPhoto() {
    return photo;
  }

  public void setPhoto(String photo) {
    this.photo = photo;
  }

  private String name;
  private Integer id;
  private String photo = null;
}
