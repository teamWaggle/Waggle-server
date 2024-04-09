package com.example.waggle.global.config.kafka;

import org.apache.kafka.clients.admin.AdminClientConfig;
import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.core.KafkaAdmin;

import java.util.HashMap;
import java.util.Map;

@Configuration
public class KafkaTopicConfig {

    @Value(value = "${kafka.bootstrapAddress}")
    private String bootstrapAddress;

    @Value("${kafka.topic.alarm.name}")
    private String topicName;
    @Value("${kafka.topic.alarm.numPartitions}")
    private String numPartitions;
    @Value("${kafka.topic.alarm.replicationFactor}")
    private String replicationFactor;


    @Bean
    public KafkaAdmin kafkaAdmin() {
        Map<String, Object> configs = new HashMap<>();
        configs.put(AdminClientConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapAddress);
        return new KafkaAdmin(configs);
    }

    @Bean
    public NewTopic newTopic() {
        return new NewTopic(topicName, Integer.parseInt(numPartitions), Short.parseShort(replicationFactor));
    }
}
