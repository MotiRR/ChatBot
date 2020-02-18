package integro;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import sber.edu.analyzer.MessageAnalyzer;
import sber.edu.database.DataBaseManager;
import sber.edu.database.dao.MessagesDAO;
import sber.edu.database.dao.SentencesDAO;
import sber.edu.kafka.messageReceiver.Message;
import sber.edu.kafka.messageReceiver.MessageTypes;
import sber.edu.service.MainClass;

import java.sql.SQLException;

import static org.junit.Assert.assertEquals;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = MainClass.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class CoreNLPTest {

    @Autowired
    MessagesDAO messagesDAO;
    @Autowired
    SentencesDAO sentencesDAO;
    MessageAnalyzer messageAnalyzer;

    @BeforeClass
    public static void prepare() {
        System.setProperty("sberAdmin.database.name", "SberAdminTestDB.db");
    }

    @Before
    public void setUp() throws SQLException {
        DataBaseManager.getInstance().clearDataBase();
        messageAnalyzer = new MessageAnalyzer();
    }

    @Test
    public void testSendMessage() throws SQLException {
        Message messageNew = new Message();
        messageNew.setFromUser(1);
        messageNew.setToUser(2);
        messageNew.setMessage("Сегодня вашему вниманию представляется реклама от мобильной компании. " +
                "Все лучшее только у нас. Кто не воспользуется нашеми услугами, тот дурак.");
        messageNew.setType(MessageTypes.TEXT);
        messageNew.setTime(System.currentTimeMillis());
        messageAnalyzer.messageReceived(messageNew);
        int sizeMessage = messagesDAO.selectAll().size();
        int sizeSentence = sentencesDAO.selectAll().size();
        assertEquals(1, sizeMessage);
        assertEquals(3, sizeSentence);
    }
}
