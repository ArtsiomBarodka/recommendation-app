package com.crypto.app.service.impl;

import com.crypto.app.exception.DataNotFoundException;
import com.crypto.app.model.dto.CryptoCurrency;
import com.crypto.app.model.dto.CryptoSymbolType;
import com.crypto.app.model.entity.CryptoCurrencyEntity;
import com.crypto.app.model.entity.CryptoSymbolEntity;
import com.crypto.app.repository.CryptoCurrencyRepository;
import com.crypto.app.repository.CryptoSymbolRepository;
import com.crypto.app.service.CryptoCurrencyDBService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class CryptoCurrencyDBServiceImpl implements CryptoCurrencyDBService {
    private final CryptoCurrencyRepository cryptoCurrencyRepository;
    private final CryptoSymbolRepository cryptoSymbolRepository;

    @Override
    @Transactional
    public void saveAll(List<CryptoCurrency> cryptoCurrencyList) {
        var symbolEntityMap = getSymbolEntityMap(cryptoCurrencyList);

        var cryptoCurrencyEntityList = cryptoCurrencyList.stream()
                .map(cryptoCurrency -> toEntity(cryptoCurrency, symbolEntityMap))
                .toList();

        cryptoCurrencyRepository.saveAll(cryptoCurrencyEntityList);
    }

    private Map<CryptoSymbolType, CryptoSymbolEntity> getSymbolEntityMap(List<CryptoCurrency> cryptoCurrencyList) {
        var symbols = cryptoCurrencyList.stream()
                .map(cryptoCurrency -> cryptoCurrency.getCryptoSymbol().getName())
                .collect(Collectors.toSet());

        var symbolEntityList = cryptoSymbolRepository.findAllByNameIn(symbols);
        return  symbolEntityList.stream().collect(Collectors.toMap(CryptoSymbolEntity::getName, symbol -> symbol));
    }


    private CryptoCurrencyEntity toEntity(CryptoCurrency source, Map<CryptoSymbolType,CryptoSymbolEntity> symbolEntityMap) {
        var cryptoSymbolName = source.getCryptoSymbol().getName();
        var cryptoSymbolEntity = symbolEntityMap.get(cryptoSymbolName);
        if(cryptoSymbolEntity == null) {
            log.error("Currency Symbol with (name = {}) is not found", cryptoSymbolName);
            throw new DataNotFoundException("Currency Symbol with (name = %s) is not found".formatted(cryptoSymbolName));
        }

        var cryptoCurrencyEntity = new CryptoCurrencyEntity();
        cryptoCurrencyEntity.setPrice(source.getPrice());
        cryptoCurrencyEntity.setTimestamp(source.getTimestamp());
        cryptoCurrencyEntity.setCryptoSymbol(cryptoSymbolEntity);
        return cryptoCurrencyEntity;
    }
}
