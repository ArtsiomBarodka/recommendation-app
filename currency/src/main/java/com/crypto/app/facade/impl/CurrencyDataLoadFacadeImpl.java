package com.crypto.app.facade.impl;

import com.crypto.app.facade.CurrencyDataLoadFacade;
import com.crypto.app.model.dto.Currency;
import com.crypto.app.model.dto.EventType;
import com.crypto.app.model.response.CurrencyDataReaderResponse;
import com.crypto.app.service.CurrencyDBService;
import com.crypto.app.service.CurrencyDataReader;
import com.crypto.app.service.NotificationService;
import com.crypto.app.service.SymbolDBService;
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
    private final SymbolDBService symbolDBService;

    public CurrencyDataLoadFacadeImpl(@Qualifier("File") CurrencyDataReader<String> fileDataReader,
                                      CurrencyDBService currencyDBService,
                                      NotificationService notificationService,
                                      SymbolDBService symbolDBService) {
        this.fileDataReader = fileDataReader;
        this.currencyDBService = currencyDBService;
        this.notificationService = notificationService;
        this.symbolDBService = symbolDBService;
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

        savedCurrencyList.forEach(savedCurrency -> notificationService.notifyAggregator(savedCurrency, EventType.ADDED));
    }

    @Override
    public void loadDataFromFiles(List<String> filePath) {
        filePath.forEach(this::loadDataFromFile);
    }

    private Currency toDto(CurrencyDataReaderResponse source) {
        var symbol = symbolDBService.getSymbolByName(source.cryptoSymbol());

        var cryptoCurrency = new Currency();
        cryptoCurrency.setPrice(source.price());
        cryptoCurrency.setTimestamp(source.timestamp());
        cryptoCurrency.setSymbol(symbol);

        return cryptoCurrency;
    }
}
