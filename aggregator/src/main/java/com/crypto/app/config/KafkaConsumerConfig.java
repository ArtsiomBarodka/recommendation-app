package com.crypto.app.config;

import com.crypto.app.model.message.CurrencyMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.common.TopicPartition;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.EnableKafkaRetryTopic;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.listener.ContainerProperties;
import org.springframework.kafka.listener.DeadLetterPublishingRecoverer;
import org.springframework.kafka.listener.DefaultErrorHandler;
import org.springframework.kafka.support.serializer.JsonDeserializer;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.util.backoff.BackOff;
import org.springframework.util.backoff.FixedBackOff;

import java.util.HashMap;
import java.util.Map;

import static java.util.concurrent.TimeUnit.SECONDS;

@Slf4j
@EnableKafkaRetryTopic
@Configuration
@RequiredArgsConstructor
public class KafkaConsumerConfig {
    private final PropertiesConfig propertiesConfig;

    private ConsumerFactory<String, CurrencyMessage> consumerCurrencyFactory() {
        JsonDeserializer<CurrencyMessage> deserializer = new JsonDeserializer<>(CurrencyMessage.class);
        deserializer.setRemoveTypeHeaders(false);
        deserializer.addTrustedPackages("*");
        deserializer.setUseTypeMapperForKey(true);

        Map<String, Object> props = new HashMap<>();
        props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, propertiesConfig.getKafkaServer());
        props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, deserializer);
        props.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, propertiesConfig.getKafkaConsumerCurrencyOffset());
        props.put(ConsumerConfig.MAX_POLL_RECORDS_CONFIG, propertiesConfig.getKafkaConsumerCurrencyMaxPollRecords());

        return new DefaultKafkaConsumerFactory<>(props, new StringDeserializer(), deserializer);
    }

    @Bean
    public ConcurrentKafkaListenerContainerFactory<String, CurrencyMessage> currencyKafkaListenerContainerFactory(DefaultErrorHandler retryErrorWithDlqHandler) {
        ConcurrentKafkaListenerContainerFactory<String, CurrencyMessage> factory =
                new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConcurrency(propertiesConfig.getKafkaConsumerCurrencyCount());
        factory.setConsumerFactory(consumerCurrencyFactory());
        factory.getContainerProperties().setAckMode(ContainerProperties.AckMode.BATCH);
        factory.setBatchListener(true);
        factory.setCommonErrorHandler(retryErrorWithDlqHandler);
        return factory;
    }

    @Bean
    public DefaultErrorHandler retryErrorWithDlqHandler(KafkaTemplate<String, CurrencyMessage> currencyDltKafkaTemplate) {
        BackOff fixedBackOff = new FixedBackOff(SECONDS.toMillis(propertiesConfig.getKafkaConsumerCurrencyRetryInterval()), propertiesConfig.getKafkaConsumerCurrencyRetryAttempts());
        var recoverer = new DeadLetterPublishingRecoverer(currencyDltKafkaTemplate,
                (consumerRecord, e) -> {
                    var message = (CurrencyMessage) consumerRecord.value();
                    log.error("Retry attempts are finished for message: {}", message);
                    log.error("The message {} goes to the Dead Letter Queue", message);
                    return new TopicPartition(propertiesConfig.getKafkaTopicCurrencyDltName(), consumerRecord.partition());
                });
        var errorHandler = new DefaultErrorHandler(recoverer, fixedBackOff);
        errorHandler.addNotRetryableExceptions(NullPointerException.class);
        return errorHandler;
    }

    @Bean
    public TaskScheduler scheduler() {
        return new ThreadPoolTaskScheduler();
    }
}
