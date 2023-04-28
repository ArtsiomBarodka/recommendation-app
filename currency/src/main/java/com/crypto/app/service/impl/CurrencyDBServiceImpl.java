package com.crypto.app.service.impl;

import com.crypto.app.exception.DataNotFoundException;
import com.crypto.app.model.dto.Currency;
import com.crypto.app.model.dto.EventType;
import com.crypto.app.model.dto.Symbol;
import com.crypto.app.model.entity.CurrencyEntity;
import com.crypto.app.model.entity.CurrencyEventEntity;
import com.crypto.app.model.entity.SymbolEntity;
import com.crypto.app.repository.CurrencyEventRepository;
import com.crypto.app.repository.SymbolRepository;
import com.crypto.app.service.CurrencyDBService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class CurrencyDBServiceImpl implements CurrencyDBService {
    private final CurrencyEventRepository currencyEventRepository;
    private final SymbolRepository symbolRepository;

    @Override
    @Transactional
    public List<Currency> saveAll(List<Currency> currencyList) {
        var currencyEventEntity = toEventEntity(currencyList, EventType.ADDED);

        var savedEvent = currencyEventRepository.save(currencyEventEntity);

        log.info("Added new Event = {}", savedEvent);

        return savedEvent.getCurrencyEntityList().stream().map(this::toCurrencyDto).toList();
    }

    private Map<String, SymbolEntity> getSymbolEntityMap(List<Currency> currencyList) {
        var symbols = currencyList.stream()
                .map(currency -> currency.getSymbol().getName())
                .collect(Collectors.toSet());

        var symbolEntityList = symbolRepository.findAllByNameIn(symbols);
        return symbolEntityList.stream().collect(Collectors.toMap(SymbolEntity::getName, symbol -> symbol));
    }


    private CurrencyEventEntity toEventEntity(List<Currency> sourceList, EventType eventType) {
        var symbolEntityMap = getSymbolEntityMap(sourceList);

        var cryptoCurrencyEntityList = sourceList.stream()
                .map(currency -> toCurrencyEntity(currency, symbolEntityMap))
                .toList();

        var currencyEventEntity = new CurrencyEventEntity();
        currencyEventEntity.setEventType(eventType);
        currencyEventEntity.setCurrencyEntityList(cryptoCurrencyEntityList);
        return currencyEventEntity;
    }

    private CurrencyEntity toCurrencyEntity(Currency source, Map<String, SymbolEntity> symbolEntityMap) {
        var cryptoSymbolName = source.getSymbol().getName();
        var cryptoSymbolEntity = symbolEntityMap.get(cryptoSymbolName);
        if (cryptoSymbolEntity == null) {
            log.error("Currency Symbol with (name = {}) is not found", cryptoSymbolName);
            throw new DataNotFoundException("Currency Symbol with (name = %s) is not found".formatted(cryptoSymbolName));
        }

        var cryptoCurrencyEntity = new CurrencyEntity();
        cryptoCurrencyEntity.setPrice(source.getPrice());
        cryptoCurrencyEntity.setTimestamp(source.getTimestamp());
        cryptoCurrencyEntity.setCryptoSymbol(cryptoSymbolEntity);
        return cryptoCurrencyEntity;
    }

    private Currency toCurrencyDto(CurrencyEntity source) {
        var symbol = new Symbol();
        symbol.setId(source.getCryptoSymbol().getId());
        symbol.setName(source.getCryptoSymbol().getName());

        var currency = new Currency();
        currency.setId(source.getId());
        currency.setTimestamp(source.getTimestamp());
        currency.setSymbol(symbol);
        currency.setPrice(source.getPrice());

        return currency;
    }
}
