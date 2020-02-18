package integro;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import sber.edu.analyzer.LocksManager;
import sber.edu.database.dao.MessagesDAO;
import sber.edu.database.dto.MessagesEntity;
import sber.edu.kafka.messageReceiver.Message;
import sber.edu.kafka.messageReceiver.MessageTypes;

import java.sql.SQLException;
import java.util.Date;
import java.util.List;
import java.util.Properties;
import java.util.concurrent.ExecutionException;

public class Pro {



    KafkaProducer<String, String> kafkaProduce;
    Properties props;

    @Before
    public void setUp() {
        props = new Properties();
        props.put("bootstrap.servers", "localhost:9092");
      //  props.put("group.id", "MessageGroup");
        props.put("key.serializer", "org.apache.kafka.common.serialization.StringSerializer");
        props.put("value.serializer", "org.apache.kafka.common.serialization.StringSerializer");
        //props.put(ProducerConfig.MAX_BLOCK_MS_CONFIG, 400);
        this.kafkaProduce = new KafkaProducer(props);
    }


    @Test
    public void testSendMessage() throws ExecutionException, InterruptedException, JsonProcessingException {
        Message messageNew = new Message();
        messageNew.setFromUser(12);
        messageNew.setToUser(2);
        messageNew.setMessage("реклама эротика. Вот так вот. Так что делать нечего.");//"реклама. наркотики. эротика"); "реклама реклама реклама"
        messageNew.setType(MessageTypes.TEXT);
        messageNew.setTime(System.currentTimeMillis());
        ProducerRecord<String, String> record = new ProducerRecord("MESSAGETOPIC", "test",
                new ObjectMapper().writeValueAsString(messageNew));
        Assert.assertNotNull(this.kafkaProduce.send(record).get());
    }
}
