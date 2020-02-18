package ru.sber.edu;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import ru.sber.edu.modules.Message;
import ru.sber.edu.modules.MessageTypes;

import java.util.Properties;
import java.util.concurrent.ExecutionException;

public class Pro {

    KafkaProducer kafkaProduce;
    Properties props;

    @Before
    public void setUp() {
        props = new Properties();
        props.put("bootstrap.servers", "localhost:9092");
      //  props.put("group.id", "MessageGroup");
        props.put("key.serializer", "org.apache.kafka.common.serialization.StringSerializer");
        props.put("value.serializer", "org.apache.kafka.common.serialization.StringSerializer");
        this.kafkaProduce = new KafkaProducer(props);
    }

    @Test
    public void testSendMessage() throws ExecutionException, InterruptedException, JsonProcessingException {
        Message messageNew = new Message();
        messageNew.setFromUser(3);
        messageNew.setToUser(4);
        messageNew.setMessage("Текст — это группа предложений, объединённых одной мыслью и темой. " +
                "Предложения в тексте могут быть связаны как лексически, так и грамматически. Кто придумал это определение дурак. " +
                "Лучше бы машину купил.");
        messageNew.setType(MessageTypes.TEXT);
        messageNew.setTime(100000L);
        ProducerRecord<String, String> record = new ProducerRecord("MESSAGETOPIC", "test",
                new ObjectMapper().writeValueAsString(messageNew));
        Assert.assertNotNull(this.kafkaProduce.send(record).get());
    }
}
