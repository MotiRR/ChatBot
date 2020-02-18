package sber.edu.database.dto;

import javax.persistence.*;

@Entity
@Table(name = "friends", schema = "main")
public class FriendEntity {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Integer id;

  @Basic
  @Column(name = "user_id")
  private Integer userId;

  @Basic
  @Column(name = "friend_id")
  private Integer friendId;

  public Integer getFriendId() {
    return friendId;
  }

  public void setFriendId(Integer friendId) {
    this.friendId = friendId;
  }

  public Integer getUserId() {
    return userId;
  }

  public void setUserId(Integer userId) {
    this.userId = userId;
  }

  public Integer getId() {
    return id;
  }

  public void setId(Integer id) {
    this.id = id;
  }
}
