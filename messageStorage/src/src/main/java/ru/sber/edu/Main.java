package ru.sber.edu;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.PropertySource;
import ru.sber.edu.kafka.KafkaConsumerRunner;
import ru.sber.edu.repository.MessageDAOImpl;


@SpringBootApplication
/*@ComponentScan(basePackages = "ru.sber.edu")*/
@PropertySource("classpath:messenger.properties")
public class Main implements CommandLineRunner {

    @Autowired
    KafkaConsumerRunner kafkaConsumerRunner;
    @Autowired
    MessageDAOImpl messageDAO;

    @Override
    public void run(String[] args) throws Exception {
        kafkaConsumerRunner.setMessageDAO(messageDAO);
        Thread thread = new Thread(kafkaConsumerRunner);
        thread.start();
    }

    public static void main(String[] args) {
        ApplicationContext context = SpringApplication.run(Main.class, args);
    }
}
