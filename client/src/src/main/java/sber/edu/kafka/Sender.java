package sber.edu.kafka;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import sber.edu.webserver.messenger.Message;

public class Sender {
  public static final Logger logger = LogManager.getLogger(Sender.class);

  @Value("${kafka.messagesKeeper.topic}")
  private String messageTopic;

  @Autowired
  private KafkaTemplate<String, Message> kafkaTemplate;

  public void send(Message message) {
    logger.info("sending " + message.toString());
    kafkaTemplate.send(messageTopic, "test", message);
  }
}