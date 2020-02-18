package sber.edu.database.dto;

import sber.edu.kafka.restrictionSender.restrictions.RestrictionsIds;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "status_of_sentence", schema = "main")
public class StatusOfSentenceEntity implements Serializable {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "id")
  private Integer id;

  @ManyToOne
  @JoinColumn(name = "id_sentence", referencedColumnName = "id")
  private SentencesEntity sentence;

  @Column(name = "status")
  private String status;

  public Integer getId() {
    return id;
  }

  public void setId(Integer id) {
    this.id = id;
  }

  public SentencesEntity getSentence() {
    return sentence;
  }

  public void setSentence(SentencesEntity sentence) {
    this.sentence = sentence;
  }

  public String getStatus() {
    return status;
  }

  public void setStatus(String status) {
    this.status = status;
  }
}
