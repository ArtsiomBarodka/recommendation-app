package com.crypto.app.config;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Getter
@Component
public class PropertiesConfig {
    @Value("${kafka.bootstrapAddress}")
    private String kafkaServer;

    @Value("${kafka.topic.currency.name}")
    private String kafkaTopicCurrencyName;

    @Value("${kafka.topic.currency-dlt.name}")
    private String kafkaTopicCurrencyDltName;

    @Value("${kafka.topic.currency-dlt.partitions}")
    private int kafkaTopicCurrencyDltPartitions;

    @Value("${kafka.topic.currency-dlt.replicas}")
    private short kafkaTopicCurrencyDltReplicas;

    @Value("${kafka.consumer.currency.offset}")
    private String kafkaConsumerCurrencyOffset;

    @Value("${kafka.consumer.currency.count}")
    private int kafkaConsumerCurrencyCount;

    @Value("${kafka.consumer.currency.max-poll-records}")
    private int kafkaConsumerCurrencyMaxPollRecords;

    @Value("${kafka.consumer.currency.retry.interval}")
    private int kafkaConsumerCurrencyRetryInterval;

    @Value("${kafka.consumer.currency.retry.attempts}")
    private int kafkaConsumerCurrencyRetryAttempts;

    @Value("${kafka.consumer.currency.aggregator.group.id}")
    private String kafkaConsumerCurrencyCourierGroupId;
}
