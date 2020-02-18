package sber.edu.service;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import sber.edu.database.DataBaseManager;
import sber.edu.kafka.messageReceiver.MessageReceiver;
import sber.edu.restrictions.RestrictionsSaver;
import sber.edu.session.SessionKeeper;
import sber.edu.session.SessionManager;
import sber.edu.webserver.common.RequestsInterceptor;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.Properties;

@SpringBootApplication
@ComponentScan(basePackages = {
  "sber.edu.webserver.auth",
  "sber.edu.webserver.profile",
  "sber.edu.webserver.community",
  "sber.edu.webserver.responses",
  "sber.edu.webserver.common",
  "sber.edu.webserver.messenger",
  "sber.edu.webserver.administration",
  "sber.edu.service",
  "sber.edu.kafka"
})
public class MainClass {
  static {
    System.setProperty("log4j.configuration", "file:src/main/resources/log4j.properties");
    System.setProperty("server.port", "8181");
  }

  static {
    try (InputStream input = new FileInputStream("src/main/resources/messenger.properties")) {
      Properties kafkaProperties = new Properties();
      kafkaProperties.load(input);

      for (String propName : kafkaProperties.stringPropertyNames()) {
        System.setProperty(propName, kafkaProperties.getProperty(propName));
      }
    } catch (IOException ex) {
      ex.printStackTrace();
    }
  }

  public static final Logger logger = LogManager.getLogger(MainClass.class);

  private static final MessageReceiver messageReceiver = MessageReceiver.getInstance();

  public static void main(String args[]) throws SQLException {
    System.setProperty("sbergift.database.name", "sberGift.db");

    messageReceiver.subscribe(new RestrictionsSaver());
    messageReceiver.start();
    SpringApplication.run(MainClass.class, args);
  }

  @Bean
  public void InitializeService() throws SQLException {
    DataBaseManager dbManager = DataBaseManager.getInstance();

    SessionKeeper sessionKeeper = SessionKeeper.getInstance();
    sessionKeeper.setSessionManager(new SessionManager());
  }

  @Bean
  public CommandLineRunner commandLineRunner(ApplicationContext ctx) {
    return args -> {
      logger.info("Let's inspect the beans provided by Spring Boot:");

      String[] beanNames = ctx.getBeanDefinitionNames();
      Arrays.sort(beanNames);
      for (String beanName : beanNames) {
        logger.info(beanName);
      }
    };
  }
}

@Configuration
class WebConfig implements WebMvcConfigurer {
  @Override
  public void addInterceptors(InterceptorRegistry registry) {
    registry.addInterceptor(new RequestsInterceptor());
  }
}