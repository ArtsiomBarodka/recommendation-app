package com.crypto.app.service.impl;

import com.crypto.app.config.PropertiesConfig;
import com.crypto.app.model.dto.Currency;
import com.crypto.app.model.dto.EventType;
import com.crypto.app.model.message.CurrencyMessage;
import com.crypto.app.model.message.SymbolMessageItem;
import com.crypto.app.service.NotificationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class NotificationServiceImpl implements NotificationService {
    private final KafkaTemplate<String, CurrencyMessage> notificationKafkaTemplate;
    private final PropertiesConfig propertiesConfig;

    @Override
    public void notifyAggregator(Currency currency, EventType eventType) {
        var topic = getTopic(eventType);
        var key = currency.getSymbol().getName();
        var message = toMessage(currency);

        log.info("Sending Currency notification to {} topic with id {} and Message: {}", topic, key, message);

        notificationKafkaTemplate.send(topic, key, message);
    }

    private CurrencyMessage toMessage(Currency source) {
        var symbolMessageItem = new SymbolMessageItem();
        symbolMessageItem.setId(source.getSymbol().getId());
        symbolMessageItem.setName(source.getSymbol().getName());

        var message = new CurrencyMessage();
        message.setCryptoSymbol(symbolMessageItem);
        message.setId(source.getId());
        message.setTimestamp(source.getTimestamp());
        message.setPrice(source.getPrice());

        return message;
    }

    private String getTopic(EventType eventType) {
        return switch (eventType) {
            case ADDED -> propertiesConfig.getKafkaTopicCurrencyName();
            case REMOVED -> propertiesConfig.getKafkaTopicCurrencyName();
            case UPDATED -> propertiesConfig.getKafkaTopicCurrencyName();
        };
    }
}
