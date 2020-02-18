package sber.edu.database.dao;

import org.hibernate.Session;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import sber.edu.database.DataBaseManager;
import sber.edu.database.dto.MessagesEntity;
import sber.edu.database.dto.UserLockEntity;
import sber.edu.kafka.restrictionSender.restrictions.RestrictionsTypes;

import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Repository
public class MessagesDAO {


    public void createMessage(MessagesEntity messagesEntity) throws SQLException {
        try (Session session = DataBaseManager.getInstance().getSession()) {
            session.beginTransaction();
            session.persist(messagesEntity);
            session.getTransaction().commit();
        }
    }

    public List<MessagesEntity> selectAll() throws SQLException {
        List messagesList;
        try (Session session = DataBaseManager.getInstance().getSession()) {
            session.beginTransaction();
            String query = "select m from MessagesEntity m";
            messagesList = session.createQuery(query)
                    .getResultList();
            session.getTransaction().commit();
        }
        return messagesList;
    }

    public List<Integer> selectIdUserToday() throws SQLException {
        List messagesList;
        try (Session session = DataBaseManager.getInstance().getSession()) {
            session.beginTransaction();
            String query = "select distinct m.userId " +
                    "from MessagesEntity m " +
                    "where m.dateOfSend >= :dayPrevious " +
                    "and m.userId not in (select u.idUser " +
                    "from UserLockEntity u " +
                    "where u.typeOfRestriction = :typeOfRestriction " +
                    "and u.dateOfLock <= :currentTime " +
                    "and u.dateOfUnlock >= :currentTime)";
            messagesList = session.createQuery(query)
                    .setParameter("typeOfRestriction", RestrictionsTypes.LOCK.toString())
                    .setParameter("dayPrevious",
                            System.currentTimeMillis() - (86400 * 1000))
                    .setParameter("currentTime", System.currentTimeMillis())
                    .getResultList();
            session.getTransaction().commit();
        }
        return messagesList;
    }

    public List<Object> selectMessagesWithStatus(List<Integer> idUser) throws SQLException {
        List messagesList;
        try (Session session = DataBaseManager.getInstance().getSession()) {
            session.beginTransaction();
            String query = "select m.userId, st.status, count(st.status) as countStatus " +
                    "from MessagesEntity m " +
                    "join m.sentences s " +
                    "join s.statusOfSentences st " +
                    "where m.userId in (:userId)" +
                    "group by m.userId, st.status";
            messagesList = session.createQuery(query)
                    .setParameterList("userId", idUser)
                    .getResultList();
            session.getTransaction().commit();
        }
        return messagesList;
    }

    public List<Object> selectMessagesWithStatusForUnblocked(List<Object> userLocks) throws SQLException {
        List messagesList = new ArrayList();
        List idUser = new ArrayList();
        List dateOfSend = new ArrayList();
        for (int i = 0; i < userLocks.size(); i++) {
            idUser.add(((Object[]) userLocks.get(i))[0]);
            dateOfSend.add(((Object[]) userLocks.get(i))[1]);
        }
        try (Session session = DataBaseManager.getInstance().getSession()) {
            session.beginTransaction();
            for (int i = 0; i < idUser.size(); i++) {
                String query = "select m.userId, st.status, count(st.status) as countStatus " +
                        "from MessagesEntity m " +
                        "join m.sentences s " +
                        "join s.statusOfSentences st " +
                        "where m.userId = :userId and m.dateOfSend > :dateOfSend " +
                        "group by m.userId, st.status";
                messagesList.addAll(session.createQuery(query)
                        .setParameter("userId", idUser.get(i))
                        .setParameter("dateOfSend", dateOfSend.get(i))
                        .getResultList());
            }
            session.getTransaction().commit();
        }
        return messagesList;
    }

}
