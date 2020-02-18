package sber.edu.database.dto;

import javassist.bytecode.ByteArray;

import javax.persistence.*;

@Entity
@Table(name = "gallery", schema = "main")
public class GalleryEntity {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Integer id;

  @Basic
  @Column(name = "user_id")
  private Integer userId;

  @Column(name = "photo")
  private byte[] photo;

  public byte[] getPhoto() {
    return photo;
  }

  public void setPhoto(byte[] photo) {
    this.photo = photo;
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
