package sber.edu.services;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import sber.edu.analyzer.EntityCreate;
import sber.edu.analyzer.LocksManager;
import sber.edu.database.dao.UserLockDAO;
import sber.edu.kafka.messageReceiver.MessageReceiver;
import sber.edu.kafka.restrictionSender.RestrictionEntity;

@Service
public class UserLockService {

    public static final Logger logger = LogManager.getLogger(MessageReceiver.class);
    private final int dayBlock = Integer.parseInt(System.getProperty("locker.dayBlock"));

    @Autowired
    private UserLockDAO userLockDAO;

    @Autowired
    private EntityCreate entityCreate;

    public boolean createBlockUser(RestrictionEntity restrictionEntity) {
        try {
            userLockDAO.createUserLock(entityCreate.createUserLockEntity(restrictionEntity,
                    LocksManager.getDayBlock()));
            return true;

        } catch (Exception ex) {
            logger.info("exception creating user lock: "+ex.getMessage());
            return false;
        }
    }

    public boolean updateBlockUser(RestrictionEntity restrictionEntity) {
        try {
            userLockDAO.unlockUser(restrictionEntity.getUserId());
            return true;

        } catch (Exception ex) {
            logger.info("user lock update exception: " + ex.getMessage());
            return false;
        }
    }


}
