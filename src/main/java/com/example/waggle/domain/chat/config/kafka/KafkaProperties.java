package com.example.waggle.domain.chat.config.kafka;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Data
@Component
@ConfigurationProperties(prefix = "kafka.waggle")
public class KafkaProperties {

    private String topic;
    private String groupId;
    private String broker;

}
