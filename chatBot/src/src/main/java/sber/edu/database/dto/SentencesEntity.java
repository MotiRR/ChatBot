package sber.edu.database.dto;

import javax.persistence.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "sentences", schema = "main")
public class SentencesEntity implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    @Basic
    @Column(name = "sentence")
    private String sentence;

    @ManyToOne
    @JoinColumn(name = "id_message", referencedColumnName = "id")
    private MessagesEntity message;

    @OneToOne(mappedBy = "sentence", cascade = CascadeType.ALL)
    private ResultAnalysisEntity resultAnalysis;

    @OneToMany(mappedBy = "sentence", cascade = CascadeType.ALL)
    private List<StatusOfSentenceEntity> statusOfSentences = new ArrayList<>();

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getSentence() {
        return sentence;
    }

    public void setSentence(String sentence) {
        this.sentence = sentence;
    }

    public MessagesEntity getMessage() {
        return message;
    }

    public void setMessage(MessagesEntity message) {
        this.message = message;
    }

    public ResultAnalysisEntity getResultAnalysis() {
        return resultAnalysis;
    }

    public void addResultAnalysis(ResultAnalysisEntity resultAnalysis) {
        this.resultAnalysis = resultAnalysis;
    }

    public List<StatusOfSentenceEntity> getStatusOfSentence() {
        return statusOfSentences;
    }

    public void addStatusOfSentence(StatusOfSentenceEntity statusOfSentence) {
        this.statusOfSentences.add(statusOfSentence);
    }
}
