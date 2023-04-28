package com.crypto.app.config;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.List;

@Getter
@Component
public class PropertiesConfig {
    @Value("${kafka.bootstrapAddress}")
    private String kafkaServer;

    @Value("${kafka.topic.currency.name}")
    private String kafkaTopicCurrencyName;

    @Value("${kafka.topic.currency.partitions}")
    private int kafkaTopicCurrencyPartitions;

    @Value("${kafka.topic.currency.replicas}")
    private int kafkaTopicCurrencyReplicas;

    @Value("${crypto.recommendation.app.currency.load-data-on-startup}")
    private boolean loadCurrencyOnStartup;
    @Value("${crypto.recommendation.app.currency.data.csv}")
    private List<String> csvDataCurrencyFiles;
}
