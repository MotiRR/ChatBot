package sber.edu.analyzer;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import sber.edu.database.dao.MessagesDAO;
import sber.edu.database.dao.UserLockDAO;
import sber.edu.database.dto.UserLockEntity;
import sber.edu.kafka.messageReceiver.MessageReceiver;
import sber.edu.kafka.restrictionSender.RestrictionEntity;
import sber.edu.kafka.restrictionSender.Sender;
import sber.edu.kafka.restrictionSender.SenderConfig;
import sber.edu.kafka.restrictionSender.restrictions.Restrictions;
import sber.edu.kafka.restrictionSender.restrictions.RestrictionsIds;
import sber.edu.kafka.restrictionSender.restrictions.RestrictionsTypes;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class LocksManager {

    public static final Logger logger = LogManager.getLogger(MessageReceiver.class);

    private Sender sender = new AnnotationConfigApplicationContext(SenderConfig.class).
            getBean(Sender.class);

    private EntityCreate entityCreate = new EntityCreate();

    private MessagesDAO messagesDAO = new MessagesDAO();
    private UserLockDAO userLockDAO = new UserLockDAO();

    RestrictionEntity restrictionEntity;

    private static final int dayBlock = Integer.parseInt(System.getProperty("locker.dayBlock"));
    private static final int numberOfWarnings = Integer.parseInt(System.getProperty("locker.numberOfWarnings"));

    public LocksManager() {
    }

    public synchronized void unlocker() throws SQLException {
        List<UserLockEntity> allLockToday = userLockDAO.selectLockToday();
        userLockDAO.unlockUsers(allLockToday);
        for (int i = 0; i < allLockToday.size(); i++) {
            SendRestriction(new RestrictionEntity(allLockToday.get(i).getIdUser(),
                    RestrictionsTypes.UNLOCK, Arrays.asList("Время блокировки окончена")));
            logger.info("Unblocked " + allLockToday.get(i).getIdUser());
        }
    }

    private void SendRestriction(RestrictionEntity restrictionEntity) throws SQLException {
        try {
            sender.send(restrictionEntity);
        } catch(Exception ex) {
            logger.info("exception send: "+ ex.getMessage());
            throw new SQLException();
        }
    }

    private void createUserLockEntityAndSend(RestrictionEntity restrictionEntity) throws SQLException {

        userLockDAO.createUserLock(entityCreate.createUserLockEntity(restrictionEntity, dayBlock));
        SendRestriction(restrictionEntity);
        logger.info("Blocked " + restrictionEntity.getUserId());
    }

    public synchronized void locker(int idFromUser) throws SQLException {

        List<Object> usersWithStatusAll = new ArrayList<>();
        List<Integer> allIdUserListToday = Arrays.asList(idFromUser);
        List<Object> idUserUnlock = userLockDAO.selectUnlockWithMaxDate(allIdUserListToday);

        if(idUserUnlock.size() > 0) {
            List<Object> usersWithStatusForUnblocked = messagesDAO.selectMessagesWithStatusForUnblocked(idUserUnlock);
            usersWithStatusAll.addAll(usersWithStatusForUnblocked);
        } else {
            List<Object> usersWithStatusAllToday = messagesDAO.selectMessagesWithStatus(allIdUserListToday);
            usersWithStatusAll.addAll(usersWithStatusAllToday);
        }

        createLock(usersWithStatusAll);
    }

    private void createLock(List<Object> usersWithStatusAll) throws SQLException {
        Long count = 0L;
        boolean send = false;
        List reasons = new ArrayList();
        Object[] userNext;
        for (int i = 0; i < usersWithStatusAll.size(); i++) {

            Object[] user = (Object[]) usersWithStatusAll.get(i);
            if (!send) {
                count = count + ((Long) user[2]);
                reasons.add(Restrictions.getTranslation((RestrictionsIds) Enum.valueOf(RestrictionsIds.class, user[1].toString())));

                if (count >= numberOfWarnings) {
                    RestrictionEntity restrictionEntity = new RestrictionEntity((int) user[0],
                            RestrictionsTypes.LOCK, reasons);
                    createUserLockEntityAndSend(restrictionEntity);
                    send = true;
                }
            }

            if (i != usersWithStatusAll.size() - 1) {
                userNext = (Object[]) usersWithStatusAll.get(i + 1);
                if (userNext[0] != user[0]) {
                    send = false;
                    count = 0L;
                    reasons.clear();
                }
            }
        }
    }

    public static int getDayBlock() {
        return dayBlock;
    }

    public static int getNumberOfWarnings() {
        return numberOfWarnings;
    }
}
