package ru.sber.edu.kafka;

import com.fasterxml.jackson.databind.JsonNode;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.support.serializer.JsonDeserializer;

import java.util.HashMap;
import java.util.Map;


@Configuration
public class ListenerConfig {


    @Value("${kafka.messagesKeeper.group}")
    private String messageGroup;

    @Value("${kafka.messagesKeeper.bootstrap-servers}")
    private String bootstrapServers;

    @Bean
    public Map<String, Object> consumererConfigs() {
        Map<String, Object> props = new HashMap<>();
        props.put("group.id", messageGroup);
        props.put("bootstrap.servers", bootstrapServers);
        props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        props.put(JsonDeserializer.TRUSTED_PACKAGES, "*");

        return props;
    }

    @Bean
    public KafkaConsumer consumerRunner() {
        return new KafkaConsumer<String, JsonNode>(consumererConfigs());
    }
}