package com.crypto.app.facade.impl;

import com.crypto.app.facade.CurrencyDataLoadFacade;
import com.crypto.app.model.SymbolType;
import com.crypto.app.model.dto.Currency;
import com.crypto.app.model.dto.Symbol;
import com.crypto.app.model.response.CryptoCurrencyDataReaderResponse;
import com.crypto.app.service.CurrencyDBService;
import com.crypto.app.service.CurrencyDataReader;
import com.crypto.app.service.NotificationService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
public class CurrencyDataLoadFacadeImpl implements CurrencyDataLoadFacade {
    private final CurrencyDataReader<String> fileDataReader;
    private final CurrencyDBService currencyDBService;
    private final NotificationService notificationService;

    public CurrencyDataLoadFacadeImpl(@Qualifier("File") CurrencyDataReader<String> fileDataReader,
                                      CurrencyDBService currencyDBService,
                                      NotificationService notificationService) {
        this.fileDataReader = fileDataReader;
        this.currencyDBService = currencyDBService;
        this.notificationService = notificationService;
    }

    @Override
    public void loadDataFromFile(String filePath) {
        log.info("Loading {} filename", filePath);
        var items = fileDataReader.read(filePath);
        log.info("Was loaded {} items from {} file. Items: {}", items.size(), filePath, items);

        var cryptoCurrencyList = items.stream()
                .map(this::toDto)
                .toList();

        var savedCurrencyList = currencyDBService.saveAll(cryptoCurrencyList);

        savedCurrencyList.forEach(notificationService::notifyAggregator);
    }

    @Override
    public void loadDataFromFile(List<String> filePath) {
        filePath.forEach(this::loadDataFromFile);
    }

    private Currency toDto(CryptoCurrencyDataReaderResponse source) {
        var cryptoSymbol = new Symbol();
        cryptoSymbol.setName(SymbolType.of(source.cryptoSymbol()));

        var cryptoCurrency = new Currency();
        cryptoCurrency.setPrice(source.price());
        cryptoCurrency.setTimestamp(source.timestamp());
        cryptoCurrency.setSymbol(cryptoSymbol);

        return cryptoCurrency;
    }
}
