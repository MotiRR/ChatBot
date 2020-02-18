package sber.edu.database.dto;

import sber.edu.kafka.restrictionSender.restrictions.RestrictionsTypes;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "user_lock", schema = "main")
public class UserLockEntity {

    @Id
    @Basic
    @Column(name = "id_user")
    private Integer idUser;

    @Basic
    @Column(name = "date_of_lock")
    private Long dateOfLock;

    @Basic
    @Column(name = "date_of_unlock")
    private Long dateOfUnlock;


    @Column(name = "type_of_restriction")
    private String typeOfRestriction;

    @Basic
    @Column(name = "reason")
    private String reason;

    public Integer getIdUser() {
        return idUser;
    }

    public void setIdUser(Integer idUser) {
        this.idUser = idUser;
    }

    public Long getDateOfLock() {
        return dateOfLock;
    }

    public void setDateOfLock(Long dateOfLock) {
        this.dateOfLock = dateOfLock;
    }

    public Long getDateOfUnlock() {
        return dateOfUnlock;
    }

    public void setDateOfUnlock(Long dateOfUnlock) {
        this.dateOfUnlock = dateOfUnlock;
    }

    public String getTypeOfRestriction() {
        return typeOfRestriction;
    }

    public void setTypeOfRestriction(String typeOfRestriction) {
        this.typeOfRestriction = typeOfRestriction;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }
}
