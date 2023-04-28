package com.crypto.app.service.impl;

import com.crypto.app.config.KafkaProducerConfig;
import com.crypto.app.config.PropertiesConfig;
import com.crypto.app.model.dto.Currency;
import com.crypto.app.model.dto.EventType;
import com.crypto.app.model.dto.Symbol;
import com.crypto.app.model.message.CurrencyMessage;
import org.apache.kafka.clients.consumer.Consumer;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.support.serializer.JsonDeserializer;
import org.springframework.kafka.test.utils.KafkaTestUtils;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.KafkaContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.utility.DockerImageName;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.Collections;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Testcontainers
@ContextConfiguration(classes = {NotificationServiceImpl.class, PropertiesConfig.class, KafkaProducerConfig.class})
public class NotificationServiceImplTest {
    private static final String TOPIC = "Test-topic";

    @Container
    private static final KafkaContainer KAFKA =
            new KafkaContainer(DockerImageName.parse("confluentinc/cp-kafka:6.2.10"));

    @DynamicPropertySource
    static void registerKafkaProperties(DynamicPropertyRegistry registry) {
        registry.add("kafka.bootstrapAddress", KAFKA::getBootstrapServers);
        registry.add("kafka.topic.currency.name", () -> TOPIC);
    }

    @Autowired
    private NotificationServiceImpl notificationService;

    @Test
    void sendNotificationTest() {
        // Arrange
        var symbol = new Symbol();
        symbol.setId(1L);
        symbol.setName("symbol");

        var currency = new Currency();
        currency.setId(1L);
        currency.setTimestamp(Timestamp.valueOf(LocalDateTime.now()));
        currency.setPrice(new BigDecimal(100));
        currency.setSymbol(symbol);

        // Act
        notificationService.notifyAggregator(currency, EventType.ADDED);

        // Assert
        try (Consumer<String, CurrencyMessage> consumer = createConsumer()) {
            ConsumerRecords<String, CurrencyMessage> records = KafkaTestUtils.getRecords(consumer);

            assertThat(records.count()).isEqualTo(1);

            ConsumerRecord<String, CurrencyMessage> record = records.iterator().next();
            assertThat(record.key()).isEqualTo(currency.getSymbol().getName());
            assertThat(record.value().getId()).isEqualTo(currency.getId());
            assertThat(record.value().getTimestamp().getTime()).isEqualTo(currency.getTimestamp().getTime());
            assertThat(record.value().getPrice()).isEqualTo(currency.getPrice());
            assertThat(record.value().getCryptoSymbol().getId()).isEqualTo(currency.getSymbol().getId());
            assertThat(record.value().getCryptoSymbol().getName()).isEqualTo(currency.getSymbol().getName());
        }
    }

    private Consumer<String, CurrencyMessage> createConsumer() {
        var consumerProps = KafkaTestUtils.consumerProps(KAFKA.getBootstrapServers(), "test-group", "false");
        Consumer<String, CurrencyMessage> consumer = new DefaultKafkaConsumerFactory<>(
                consumerProps,
                new StringDeserializer(),
                new JsonDeserializer<>(CurrencyMessage.class))
                .createConsumer();

        consumer.subscribe(Collections.singletonList(TOPIC));

        return consumer;
    }
}
