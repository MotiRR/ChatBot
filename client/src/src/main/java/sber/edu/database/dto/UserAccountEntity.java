package sber.edu.database.dto;

import javax.persistence.*;
import java.util.Objects;

@Entity
@Table(name = "user_account", schema = "main", catalog = "")
public class UserAccountEntity {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "id")
  private Integer id;

  @Basic
  @Column(name = "login")
  private String login;

  @Basic
  @Column(name = "password")
  private String password;

  public Integer getId() {
    return id;
  }

  public void setId(Integer id) {
    this.id = id;
  }


  public String getLogin() {
    return login;
  }

  public void setLogin(String login) {
    this.login = login;
  }

  public String getPassword() {
    return password;
  }

  public void setPassword(String password) {
    this.password = password;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    UserAccountEntity that = (UserAccountEntity) o;
    return Objects.equals(login, that.login) &&
      Objects.equals(password, that.password);
  }

  @Override
  public int hashCode() {
    return Objects.hash(login, password);
  }
}
