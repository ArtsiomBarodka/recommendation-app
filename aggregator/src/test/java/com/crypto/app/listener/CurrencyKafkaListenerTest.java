package com.crypto.app.listener;

import com.crypto.app.config.KafkaConsumerConfig;
import com.crypto.app.config.KafkaProducerConfig;
import com.crypto.app.config.PropertiesConfig;
import com.crypto.app.model.message.CurrencyMessage;
import com.crypto.app.model.message.SymbolMessageItem;
import com.crypto.app.service.CurrencyDBService;
import org.apache.kafka.common.serialization.StringSerializer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;
import org.springframework.kafka.support.serializer.JsonSerializer;
import org.springframework.kafka.test.utils.KafkaTestUtils;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.KafkaContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.shaded.org.awaitility.Awaitility;
import org.testcontainers.utility.DockerImageName;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Map;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@SpringBootTest
@ExtendWith(MockitoExtension.class)
@Testcontainers
@ContextConfiguration(classes = {CurrencyKafkaListenerTest.TestConfig.class,
        PropertiesConfig.class, KafkaConsumerConfig.class,
        KafkaProducerConfig.class, CurrencyKafkaListener.class})
public class CurrencyKafkaListenerTest {
    private static final String TOPIC = "Test-topic";

    @Container
    private static final KafkaContainer KAFKA =
            new KafkaContainer(DockerImageName.parse("confluentinc/cp-kafka:6.2.10"));


    @DynamicPropertySource
    static void registerKafkaProperties(DynamicPropertyRegistry registry) {
        registry.add("kafka.bootstrapAddress", KAFKA::getBootstrapServers);
        registry.add("kafka.topic.currency.name", () -> TOPIC);
        registry.add("kafka.consumer.currency.offset", () -> "earliest");
        registry.add("kafka.consumer.currency.retry.interval", () -> "1");
        registry.add("kafka.consumer.currency.retry.attempts", () -> "3");
    }

    @Autowired
    private KafkaTemplate<String, CurrencyMessage> kafkaTemplate;

    @MockBean
    private CurrencyDBService currencyDBService;

    @Test
    void processAddedDataTest() {
        // Arrange
        var key = "key";
        var notificationMessage = getCurrencyMessage();

        // Act
        kafkaTemplate.send(TOPIC, key, notificationMessage);

        // Assert
        Awaitility.await().atMost(Duration.ofSeconds(10)).untilAsserted(() -> {
            verify(currencyDBService, times(1)).saveAll(any());
        });
    }

    @Test
    void processAddedDataTest_Retry() {
        // Arrange
        var key = "key";
        var notificationMessage = getCurrencyMessage();

        when(currencyDBService.saveAll(any())).thenThrow(new RuntimeException());

        // Act
        kafkaTemplate.send(TOPIC, key, notificationMessage);

        // Assert
        Awaitility.await().atMost(Duration.ofSeconds(10)).untilAsserted(() -> {
            verify(currencyDBService, times(4)).saveAll(any());
        });
    }

    private CurrencyMessage getCurrencyMessage() {
        var symbolMessageItem = new SymbolMessageItem();
        symbolMessageItem.setId(1L);
        symbolMessageItem.setName("name");

        var notificationMessage = new CurrencyMessage();
        notificationMessage.setId(1L);
        notificationMessage.setTimestamp(Timestamp.valueOf(LocalDateTime.now()));
        notificationMessage.setCryptoSymbol(symbolMessageItem);
        notificationMessage.setPrice(BigDecimal.valueOf(1));

        return notificationMessage;
    }

    @TestConfiguration
    public static class TestConfig {
        @Bean
        public KafkaTemplate<String, CurrencyMessage> kafkaTemplate() {
            Map<String, Object> producerProps = KafkaTestUtils.producerProps(KAFKA.getBootstrapServers());
            ProducerFactory<String, CurrencyMessage> producerFactory =
                    new DefaultKafkaProducerFactory<>(
                            producerProps,
                            new StringSerializer(),
                            new JsonSerializer<>());
            return new KafkaTemplate<>(producerFactory);
        }
    }
}
