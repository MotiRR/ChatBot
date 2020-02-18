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
import sber.edu.analyzer.MessageAnalyzer;
import sber.edu.analyzer.WordsConf;
import sber.edu.database.DataBaseManager;
import sber.edu.kafka.messageReceiver.MessageReceiver;
import sber.edu.webserver.common.RequestsInterceptor;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.Properties;

@SpringBootApplication
@ComponentScan(basePackages = {
        "sber.edu.webserver.admin",
        "sber.edu.kafka",
        "sber.edu.services",
        "sber.edu.database.dao",
        "sber.edu.controllers",
        "sber.edu.analyzer"
})
public class MainClass {
    static {
        System.setProperty("log4j.configuration", "file:src/main/resources/log4j.properties");
        System.setProperty("server.port", "8585");
    }

    static {
        try (InputStream input = new FileInputStream("src/main/resources/chatBot.properties")) {
            Properties kafkaProperties = new Properties();
            kafkaProperties.load(input);

            for (String propName : kafkaProperties.stringPropertyNames()) {
                System.setProperty(propName, kafkaProperties.getProperty(propName));
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    private final static MessageReceiver messageReceiver = MessageReceiver.getInstance();

    public static final Logger logger = LogManager.getLogger(MainClass.class);

    public static void main(String args[]) throws SQLException {
        System.setProperty("sberAdmin.database.name", "sberAdmin.db");

        DataBaseManager.getInstance();

        messageReceiver.subscribe(new MessageAnalyzer());
        messageReceiver.start();

        WordsConf.createWordsForStarts();

        SpringApplication.run(MainClass.class, args);
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