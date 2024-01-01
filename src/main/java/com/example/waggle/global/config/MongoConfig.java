package com.example.waggle.global.config;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

@RequiredArgsConstructor
@EnableMongoRepositories(basePackages = "com.example.waggle.domain.chat")
@Configuration
public class MongoConfig {

    private final MongoWaggleProperties mongoWaggleProperties;

    @Bean
    public MongoClient mongoClient() {
        return MongoClients.create(mongoWaggleProperties.getClient());
    }

    @Bean
    public MongoTemplate mongoTemplate() {
        return new MongoTemplate(mongoClient(), mongoWaggleProperties.getName());
    }

}
