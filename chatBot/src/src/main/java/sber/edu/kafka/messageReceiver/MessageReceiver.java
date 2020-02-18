package sber.edu.kafka.messageReceiver;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import java.time.Duration;
import java.util.*;
import java.util.concurrent.atomic.AtomicBoolean;

public class MessageReceiver {
  public static final Logger logger = LogManager.getLogger(MessageReceiver.class);

  private final String bootstrapServers;
  private final String topic;

  private static final MessageReceiver instance = new MessageReceiver();

  private KafkaConsumer<String, String> kafkaConsumer;
  private final AtomicBoolean isInterrupted = new AtomicBoolean(false);
  private final Thread receiverThread;

  private final List<MessageListener> listeners = Collections.synchronizedList(new ArrayList<>());

  public static MessageReceiver getInstance() {
    return instance;
  }

  public synchronized void start() {
    if (isInterrupted.get()) {
      throw new IllegalStateException("Method MessageReceiver::start used after receiver was interrupted");
    }
    logger.info("MessageReceiver started successfully");
    receiverThread.start();
  }

  public synchronized void stop() throws InterruptedException {
    if (isInterrupted.get()) {
      throw new IllegalStateException("Method MessageReceiver::stop used after receiver was interrupted");
    }

    isInterrupted.set(true);

    receiverThread.join();
    kafkaConsumer.close();

    logger.info("MessageReceiver finished successfully");
  }

  public void subscribe(MessageListener listener) {
    listeners.add(listener);
  }

  private MessageReceiver() {
    bootstrapServers = System.getProperty("kafka.messages.bootstrap-servers");
    topic = System.getProperty("kafka.messages.topic");

    kafkaConsumer = new KafkaConsumer<>(consumerConfigs());
    kafkaConsumer.subscribe(Arrays.asList(topic));

    receiverThread = new Thread(getReceiver());
  }

  private Runnable getReceiver() {
    return () -> {
      while (!isInterrupted.get()) {
        try {
          ConsumerRecords<String, String> records = kafkaConsumer.poll(Duration.ofMillis(100));
          for (ConsumerRecord<String, String> record : records) {
            String valueStr = record.value();
            logger.info("received record: " + valueStr);

            ObjectMapper objectMapper = new ObjectMapper();
            Message message = objectMapper.readValue(record.value(), Message.class);

            for (MessageListener listener : listeners) {
              listener.messageReceived(message);
            }
          }
          Thread.sleep(100);
        } catch (Exception ex) {
          logger.error("exception occurred: " + ex.getMessage());
        }
      }
    };
  }

  private Map<String, Object> consumerConfigs() {
    Map<String, Object> props = new HashMap<>();

    props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
    props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
    props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
    props.put("group.id", "MessageBot");

    return props;
  }
}
