package sber.edu.database.dto;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "result_analysis", schema = "main")
public class ResultAnalysisEntity implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    @OneToOne
    @JoinColumn(name = "id_sentence", referencedColumnName = "id")
    private SentencesEntity sentence;

    @Basic
    @Column(name = "verb")
    private String verb;

    @Basic
    @Column(name = "noun")
    private String noun;

    @Basic
    @Column(name = "swear")
    private String swear;

    @Basic
    @Column(name = "xxx")
    private String xxx;

    @Basic
    @Column(name = "spam")
    private String spam;

    @Basic
    @Column(name = "secret")
    private String secret;

    public String getVerb() {
        return verb;
    }

    public void setVerb(String verb) {
        this.verb = verb;
    }

    public String getNoun() {
        return noun;
    }

    public void setNoun(String noun) {
        this.noun = noun;
    }

    public String getSwear() {
        return swear;
    }

    public void setSwear(String swear) {
        this.swear = swear;
    }

    public String getXxx() {
        return xxx;
    }

    public void setXxx(String xxx) {
        this.xxx = xxx;
    }

    public String getSpam() {
        return spam;
    }

    public void setSpam(String spam) {
        this.spam = spam;
    }

    public String getSecret() {
        return secret;
    }

    public void setSecret(String secret) {
        this.secret = secret;
    }

    public SentencesEntity getIdSentence() {
        return sentence;
    }

    public void setSentence(SentencesEntity sentence) {
        this.sentence = sentence;
    }
}
