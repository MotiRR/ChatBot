package sber.edu.database.dto;

import lombok.Data;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

import javax.persistence.*;
import java.util.List;

@Entity
@Data
@Table(name = "user", schema = "main", catalog = "")
public class UserEntity {
  @Id
  @Basic
  @Column(name = "id")
  private Integer id;

  @OneToMany(fetch = FetchType.EAGER)
  @Fetch(value = FetchMode.SUBSELECT)
  @JoinColumn(name = "user_id", referencedColumnName = "id")
  private List<GalleryEntity> photo;

  @OneToMany(fetch = FetchType.EAGER)
  @Fetch(value = FetchMode.SUBSELECT)
  @JoinColumn(name = "user_id", referencedColumnName = "id")
  private List<FriendEntity> friends;

  @Basic
  @Column(name = "name")
  private String name;

  @Basic
  @Column(name = "last_name")
  private String lastName;

  @Basic
  @Column(name = "second_name")
  private String secondName;

  @Basic
  @Column(name = "age")
  private short age;

  @Basic
  @Column(name = "phone")
  private String phone;

  @Basic
  @Column(name = "email")
  private String email;

  @Basic
  @Column(name = "is_blocked")
  private boolean isBlocked;

  @Basic
  @Column(name = "blocking_description")
  private String blockingDescription;
}
