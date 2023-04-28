package com.crypto.app.config;

import lombok.RequiredArgsConstructor;
import org.apache.kafka.clients.admin.AdminClientConfig;
import org.apache.kafka.clients.admin.NewTopic;
import org.apache.kafka.common.config.TopicConfig;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.TopicBuilder;
import org.springframework.kafka.core.KafkaAdmin;

import java.util.Map;

@Configuration
@RequiredArgsConstructor
public class KafkaTopicsConfig {
    private final PropertiesConfig propertiesConfig;

    @Bean
    public KafkaAdmin kafkaAdmin() {
        final Map<String, Object> configs = Map.of(AdminClientConfig.BOOTSTRAP_SERVERS_CONFIG, propertiesConfig.getKafkaServer());
        return new KafkaAdmin(configs);
    }

    @Bean
    public NewTopic createCurrencyDltTopic() {
        return TopicBuilder.name(propertiesConfig.getKafkaTopicCurrencyDltName())
                .partitions(propertiesConfig.getKafkaTopicCurrencyDltPartitions())
                .replicas(propertiesConfig.getKafkaTopicCurrencyDltReplicas())
                .config(TopicConfig.COMPRESSION_TYPE_CONFIG, "zstd")
                .build();
    }
}
