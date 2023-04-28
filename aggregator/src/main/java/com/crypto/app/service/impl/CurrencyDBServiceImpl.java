package com.crypto.app.service.impl;

import com.crypto.app.exception.DataNotFoundException;
import com.crypto.app.model.SymbolType;
import com.crypto.app.model.dto.Currency;
import com.crypto.app.model.dto.Symbol;
import com.crypto.app.model.entity.CurrencyEntity;
import com.crypto.app.model.entity.SymbolEntity;
import com.crypto.app.repository.CurrencyRepository;
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
    private final CurrencyRepository currencyRepository;
    private final SymbolRepository symbolRepository;

    @Override
    @Transactional
    public List<Currency> saveAll(List<Currency> currencyList) {
        var symbolEntityMap = getSymbolEntityMap(currencyList);

        var cryptoCurrencyEntityList = currencyList.stream()
                .map(currency -> toEntity(currency, symbolEntityMap))
                .toList();

        var savedCryptoCurrencyEntityList = currencyRepository.saveAll(cryptoCurrencyEntityList);

        log.info("Saved to db currency List. Currencies: {}", savedCryptoCurrencyEntityList);

        return savedCryptoCurrencyEntityList.stream().map(this::toDto).toList();
    }

    private Map<SymbolType, SymbolEntity> getSymbolEntityMap(List<Currency> currencyList) {
        var symbols = currencyList.stream()
                .map(currency -> currency.getSymbol().getName())
                .collect(Collectors.toSet());

        var symbolEntityList = symbolRepository.findAllByNameIn(symbols);
        return symbolEntityList.stream().collect(Collectors.toMap(SymbolEntity::getName, symbol -> symbol));
    }


    private CurrencyEntity toEntity(Currency source, Map<SymbolType, SymbolEntity> symbolEntityMap) {
        var cryptoSymbolName = source.getSymbol().getName();
        var cryptoSymbolEntity = symbolEntityMap.get(cryptoSymbolName);
        if (cryptoSymbolEntity == null) {
            log.error("Currency Symbol with (name = {}) is not found", cryptoSymbolName);
            throw new DataNotFoundException("Currency Symbol with (name = %s) is not found".formatted(cryptoSymbolName));
        }

        var cryptoCurrencyEntity = new CurrencyEntity();
        cryptoCurrencyEntity.setId(source.getId());
        cryptoCurrencyEntity.setPrice(source.getPrice());
        cryptoCurrencyEntity.setTimestamp(source.getTimestamp());
        cryptoCurrencyEntity.setSymbol(cryptoSymbolEntity);
        return cryptoCurrencyEntity;
    }

    private Currency toDto(CurrencyEntity source) {
        var symbol = new Symbol();
        symbol.setId(source.getSymbol().getId());
        symbol.setName(source.getSymbol().getName());

        var currency = new Currency();
        currency.setId(source.getId());
        currency.setTimestamp(source.getTimestamp());
        currency.setSymbol(symbol);
        currency.setPrice(source.getPrice());

        return currency;
    }
}
