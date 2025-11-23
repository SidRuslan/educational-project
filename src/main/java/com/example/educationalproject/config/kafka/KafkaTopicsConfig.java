package com.example.educationalproject.config.kafka;

import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.TopicBuilder;

@Configuration
public class KafkaTopicsConfig {

    @Value("${app.kafka.topics.currency-rate-created}")
    private String currencyRateCreatedTopic;

    @Value("${app.kafka.topics.currency-rate-updated}")
    private String currencyRateUpdatedTopic;

    @Value("${app.kafka.topics.currency-rate-deleted}")
    private String currencyRateDeletedTopic;

    @Bean
    public NewTopic currencyRateCreatedTopic() {
        return TopicBuilder.name(currencyRateCreatedTopic)
                .partitions(3)
                .replicas(1)
                .build();
    }

    @Bean
    public NewTopic currencyRateUpdatedTopic() {
        return TopicBuilder.name(currencyRateUpdatedTopic)
                .partitions(3)
                .replicas(1)
                .build();
    }

    @Bean
    public NewTopic currencyRateDeletedTopic() {
        return TopicBuilder.name(currencyRateDeletedTopic)
                .partitions(3)
                .replicas(1)
                .build();
    }
}
