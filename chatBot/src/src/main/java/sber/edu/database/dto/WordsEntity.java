package sber.edu.database.dto;

import sber.edu.kafka.restrictionSender.restrictions.RestrictionsIds;

import javax.persistence.*;

@Entity
@Table(name = "words", schema = "main")
public class WordsEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;


    @Column(name = "status")
    private String status;

    @Basic
    @Column(name = "word")
    private String word;

    public String getWord() {
        return word;
    }

    public void setWord(String word) {
        this.word = word;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }
}
