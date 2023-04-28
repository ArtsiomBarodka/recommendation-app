package com.crypto.app.listener;

import com.crypto.app.model.dto.Currency;
import com.crypto.app.model.dto.Symbol;
import com.crypto.app.model.message.CurrencyMessage;
import com.crypto.app.service.CurrencyDBService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@Slf4j
@RequiredArgsConstructor
public class CurrencyKafkaListener {
    private final CurrencyDBService currencyDBService;

    @KafkaListener(topics = "#{'${kafka.topic.currency.name}'.split(',')}",
            groupId = "${kafka.consumer.currency.aggregator.group.id}",
            containerFactory = "currencyKafkaListenerContainerFactory")
    public void processAddedData(List<CurrencyMessage> currencyMessageList,
                             @Header(KafkaHeaders.RECEIVED_TOPIC) String topic,
                             @Header(KafkaHeaders.RECEIVED_KEY) String key) {
        log.info("New Notification List is received: topics = {}, keys = {}, items count = {}, currencyList = {}", topic, key, currencyMessageList.size(), currencyMessageList);
        var currencyList = currencyMessageList.stream().map(this::toDto).toList();
        currencyDBService.saveAll(currencyList);
    }

    public Currency toDto(CurrencyMessage source) {
        var symbol = new Symbol();
        symbol.setId(source.getCryptoSymbol().getId());
        symbol.setName(source.getCryptoSymbol().getName());

        var currency = new Currency();
        currency.setId(source.getId());
        currency.setTimestamp(source.getTimestamp());
        currency.setPrice(source.getPrice());
        currency.setSymbol(symbol);

        return currency;
    }
}
