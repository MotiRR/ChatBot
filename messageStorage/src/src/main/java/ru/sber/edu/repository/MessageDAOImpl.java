package ru.sber.edu.repository;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import ru.sber.edu.modules.Message;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

@Repository
@Transactional
public class MessageDAOImpl implements MessageDAO {

    @PersistenceContext
    private EntityManager em;

    @Transactional
    public void save(Message message) {
        em.persist(message);
    }

    @Transactional(readOnly = true)
    public List<Message> findByFromUserWithDate(int id, Long date) {
        return em.createQuery("Select m from Message m "+
                "where m.time >= :date and m.fromUser = :id ORDER BY m.time")
                .setParameter("date", date)
                .setParameter("id", id)
                .getResultList();
    }

    @Transactional(readOnly = true)
    public List<Message> findByFromUserToUserWithDate(int idFromUser, int idToUser, Long date) {
        return em.createQuery("Select m from Message m "+
                "where m.time >= :date " +
                "and ((m.fromUser = :idFromUser and m.toUser = :idToUser) " +
                "or (m.fromUser = :idToUser and m.toUser = :idFromUser)) ORDER BY m.time")
                .setParameter("date", date)
                .setParameter("idFromUser", idFromUser)
                .setParameter("idToUser", idToUser)
                .getResultList();
    }


}
