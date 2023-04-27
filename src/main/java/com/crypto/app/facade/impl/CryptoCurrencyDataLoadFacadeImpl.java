package com.crypto.app.facade.impl;

import com.crypto.app.facade.CryptoCurrencyDataLoadFacade;
import com.crypto.app.model.dto.CryptoSymbolType;
import com.crypto.app.model.dto.CryptoCurrency;
import com.crypto.app.model.dto.CryptoSymbol;
import com.crypto.app.model.response.CryptoCurrencyDataReaderResponse;
import com.crypto.app.service.CryptoCurrencyDataReader;
import com.crypto.app.service.CryptoCurrencyDBService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
public class CryptoCurrencyDataLoadFacadeImpl implements CryptoCurrencyDataLoadFacade {
    private final CryptoCurrencyDataReader<String> csvDataReader;
    private final CryptoCurrencyDBService cryptoCurrencyDBService;

    public CryptoCurrencyDataLoadFacadeImpl(@Qualifier("CSV") CryptoCurrencyDataReader<String> csvDataReader, CryptoCurrencyDBService cryptoCurrencyDBService) {
        this.csvDataReader = csvDataReader;
        this.cryptoCurrencyDBService = cryptoCurrencyDBService;
    }

    @Override
    public void loadDataFromCSV(String filePath) {
        log.info("Loading {} filename", filePath);
        var items = csvDataReader.read(filePath);
        log.info("Was loaded {} items from {} file. Items: {}", items.size(), filePath, items);

        var cryptoCurrencyList = items.stream()
                .map(this::toDto)
                .toList();

        cryptoCurrencyDBService.saveAll(cryptoCurrencyList);
    }

    @Override
    public void loadDataFromCSV(List<String> filePath) {
        filePath.forEach(this::loadDataFromCSV);
    }

    private CryptoCurrency toDto(CryptoCurrencyDataReaderResponse source) {
        var cryptoSymbol = new CryptoSymbol();
        cryptoSymbol.setName(CryptoSymbolType.of(source.cryptoSymbol()));

        var cryptoCurrency = new CryptoCurrency();
        cryptoCurrency.setPrice(source.price());
        cryptoCurrency.setTimestamp(source.timestamp());
        cryptoCurrency.setCryptoSymbol(cryptoSymbol);

        return cryptoCurrency;
    }
}
