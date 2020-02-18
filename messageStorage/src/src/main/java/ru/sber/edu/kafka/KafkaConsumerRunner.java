package ru.sber.edu.kafka;


import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.errors.WakeupException;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.support.serializer.JsonDeserializer;
import org.springframework.stereotype.Service;
import ru.sber.edu.modules.Message;
import ru.sber.edu.repository.MessageDAO;

import java.time.Duration;
import java.util.Arrays;
import java.util.Properties;
import java.util.concurrent.atomic.AtomicBoolean;


@Configuration
@Service("KafkaConsumerRunner")
public class KafkaConsumerRunner implements Runnable {

    private final AtomicBoolean closed = new AtomicBoolean(false);

    @Autowired
    KafkaConsumer consumer;

    private MessageDAO messageDAO;

    @Value("${kafka.messagesKeeper.topic}")
    private String messageTopic;

    public void setMessageDAO(MessageDAO messageDAO) {
        this.messageDAO = messageDAO;
    }

    public KafkaConsumerRunner() {}

    public void run() {
        try {

            consumer.subscribe(Arrays.asList(messageTopic));
            while (!closed.get()) {
                ConsumerRecords<String, String> records = consumer.poll(Duration.ofMillis(100));
                for (ConsumerRecord<String, String> record : records) {
                    try {
                        System.out.println("Received: " + record.value());

                        ObjectMapper objectMapper = new ObjectMapper();
                        Message message = objectMapper.readValue(record.value(), Message.class);

                        messageDAO.save(message);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        } catch (WakeupException e) {
            // Ignore exception if closing
            if (!closed.get()) throw e;
        } finally {
            consumer.close();
        }
    }

    // Shutdown hook which can be called from a separate thread
    public void shutdown() {
        closed.set(true);
        consumer.wakeup();
    }
}
