package sber.edu.kafka.restrictionSender;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.util.concurrent.ExecutionException;

public class Sender {
  public static final Logger logger = LogManager.getLogger(Sender.class);

  @Value("${kafka.restrictions.topic}")
  private String restrictionTopic;

  @Autowired
  private KafkaTemplate<String, RestrictionEntity> kafkaTemplate;

  public void send(RestrictionEntity restrictionEntity) {
    logger.info("sending restriction: " + restrictionEntity.toString());
    kafkaTemplate.send(restrictionTopic, "test", restrictionEntity);
  }
}