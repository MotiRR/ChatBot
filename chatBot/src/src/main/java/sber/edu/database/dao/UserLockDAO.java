package sber.edu.database.dao;

import org.apache.catalina.User;
import org.hibernate.Session;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import sber.edu.database.DataBaseManager;
import sber.edu.database.dto.MessagesEntity;
import sber.edu.database.dto.UserLockEntity;
import sber.edu.database.dto.WordsEntity;
import sber.edu.kafka.restrictionSender.restrictions.RestrictionsTypes;

import javax.persistence.criteria.CriteriaBuilder;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Repository
public class UserLockDAO {

    public void createUserLock(UserLockEntity userLock) throws SQLException {
        try (Session session = DataBaseManager.getInstance().getSession()) {
            session.beginTransaction();
            session.persist(userLock);
            session.getTransaction().commit();
        }
    }

    public List<UserLockEntity> selectLockToday() throws SQLException {
        List unlockList;
        try (Session session = DataBaseManager.getInstance().getSession()) {
            session.beginTransaction();
            String query = "select u " +
                    "from UserLockEntity u " +
                    "where u.dateOfUnlock <= :currentTime and u.typeOfRestriction = :typeOfRestriction";
            unlockList = session.createQuery(query)
                    .setParameter("currentTime", System.currentTimeMillis())
                    .setParameter("typeOfRestriction", RestrictionsTypes.LOCK.toString())
                    .getResultList();
            session.getTransaction().commit();
        }
        return unlockList;
    }

    public void unlockUser(int userId) throws SQLException {
        try (Session session = DataBaseManager.getInstance().getSession()) {
            session.beginTransaction();
            String query = "update UserLockEntity u " +
                    "set u.typeOfRestriction = :typeOfRestriction, u.dateOfUnlock = :dateOfUnlock " +
                    "where u.idUser = :idUser " +
                    "and u.dateOfUnlock = (select max(u.dateOfUnlock) as dateOfUnlock from UserLockEntity u " +
                    "where u.typeOfRestriction = :typeOfRestrictionLock and u.idUser = :idUser " +
                    "group by u.idUser)";
            session.createQuery(query)
                    .setParameter("idUser", userId)
                    .setParameter("dateOfUnlock", System.currentTimeMillis())
                    .setParameter("typeOfRestriction", RestrictionsTypes.UNLOCK.toString())
                    .setParameter("typeOfRestrictionLock", RestrictionsTypes.LOCK.toString())
                    .executeUpdate();
            session.getTransaction().commit();
        }
    }

    public void unlockUsers(List<UserLockEntity> userLocks) throws SQLException {
        List userId = new ArrayList();
        List dateOfUnlock = new ArrayList();
        for(int i =0; i < userLocks.size(); i++) {
            userId.add(userLocks.get(i).getIdUser());
            dateOfUnlock.add(userLocks.get(i).getDateOfUnlock());
        }
        try (Session session = DataBaseManager.getInstance().getSession()) {
            session.beginTransaction();
            String query = "update UserLockEntity u set u.typeOfRestriction = :typeOfRestriction " +
                    "where u.idUser in (:idUser) " +
                    "and u.dateOfUnlock in (:dateOfUnlock) " +
                    "and u.typeOfRestriction = :typeOfRestrictionLock";
            session.createQuery(query)
                    .setParameter("idUser", userId)
                    .setParameter("dateOfUnlock", dateOfUnlock)
                    .setParameter("typeOfRestriction", RestrictionsTypes.UNLOCK.toString())
                    .setParameter("typeOfRestrictionLock", RestrictionsTypes.LOCK.toString())
                    .executeUpdate();
            session.getTransaction().commit();
        }
    }

    public List<Object> selectUnlockWithMaxDate(List<Integer> messagesEntity) throws SQLException {
        List unlockList;
        try (Session session = DataBaseManager.getInstance().getSession()) {
            session.beginTransaction();
            String query = "select u.id, max(u.dateOfUnlock) as dateOfUnlock " +
                    "from UserLockEntity u " +
                    "where u.id in (:idUser) and u.typeOfRestriction = :typeOfRestriction " +
                    "group by u.id";
            unlockList = session.createQuery(query)
                    .setParameterList("idUser", messagesEntity)
                    .setParameter("typeOfRestriction", RestrictionsTypes.UNLOCK.toString())
                    .getResultList();
            session.getTransaction().commit();
        }
        return unlockList;
    }


}
