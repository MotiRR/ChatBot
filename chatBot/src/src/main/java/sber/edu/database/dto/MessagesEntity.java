package sber.edu.database.dto;

import javax.persistence.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "messages", schema = "main")
public class MessagesEntity implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    @Column(name = "user_id")
    private Integer userId;

    @Column(name = "date_of_send")
    private Long dateOfSend;

    @Basic
    @Column(name = "message")
    private String message;

    @OneToMany(mappedBy = "message", cascade = CascadeType.ALL)
    private List<SentencesEntity> sentences = new ArrayList<>();

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public Long getDateOfSend() {
        return dateOfSend;
    }

    public void setDateOfSend(Long dateOfSend) {
        this.dateOfSend = dateOfSend;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public List<SentencesEntity> getSentences() {
        return sentences;
    }

    public void addSentences(SentencesEntity sentence) {
        this.sentences.add(sentence);
    }

}
