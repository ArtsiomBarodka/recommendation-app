package com.crypto.app.facade.impl;

import com.crypto.app.exception.DataNotFoundException;
import com.crypto.app.model.SymbolType;
import com.crypto.app.model.dto.Currency;
import com.crypto.app.model.response.CurrencyDataReaderResponse;
import com.crypto.app.service.CurrencyDBService;
import com.crypto.app.service.CurrencyDataReader;
import com.crypto.app.service.NotificationService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class CurrencyDataLoadFacadeImplTest {
    @Mock
    private CurrencyDataReader<String> fileDataReader;

    @Mock
    private CurrencyDBService currencyDBService;

    @Mock
    private NotificationService notificationService;

    @InjectMocks
    private CurrencyDataLoadFacadeImpl currencyDataLoadFacade;

    @Test
    void loadDataFromFile() {
        var filePath = "path";
        var cryptoCurrencyDataReaderResponse = getSymbol(SymbolType.DOGE.name());

        when(fileDataReader.read(any())).thenReturn(List.of(cryptoCurrencyDataReaderResponse));
        when(currencyDBService.saveAll(any())).thenReturn(List.of(new Currency()));
        doNothing().when(notificationService).notifyAggregator(any());

        currencyDataLoadFacade.loadDataFromFile(filePath);

        verify(fileDataReader, times(1)).read(eq(filePath));
        verify(currencyDBService, times(1)).saveAll(any());
        verify(notificationService, times(1)).notifyAggregator(any());
    }

    @Test
    void loadDataFromFile_symbolIsNotExist() {
        var filePath = "path";
        var cryptoCurrencyDataReaderResponse = getSymbol("Symbol");

        when(fileDataReader.read(any())).thenReturn(List.of(cryptoCurrencyDataReaderResponse));

        assertThrows(DataNotFoundException.class, () -> currencyDataLoadFacade.loadDataFromFile(filePath));
    }

    @Test
    void loadDataFromFiles() {
        var filePath = "path";
        var cryptoCurrencyDataReaderResponse = getSymbol(SymbolType.DOGE.name());

        when(fileDataReader.read(any())).thenReturn(List.of(cryptoCurrencyDataReaderResponse));
        when(currencyDBService.saveAll(any())).thenReturn(List.of(new Currency()));
        doNothing().when(notificationService).notifyAggregator(any());

        currencyDataLoadFacade.loadDataFromFiles(List.of(filePath));

        verify(fileDataReader, times(1)).read(eq(filePath));
        verify(currencyDBService, times(1)).saveAll(any());
        verify(notificationService, times(1)).notifyAggregator(any());
    }

    @Test
    void loadDataFromFiles_symbolIsNotExist() {
        var filePath = "path";
        var cryptoCurrencyDataReaderResponse = getSymbol("Symbol");

        when(fileDataReader.read(any())).thenReturn(List.of(cryptoCurrencyDataReaderResponse));

        assertThrows(DataNotFoundException.class, () -> currencyDataLoadFacade.loadDataFromFiles(List.of(filePath)));
    }

    private CurrencyDataReaderResponse getSymbol(String symbolName) {
        return new CurrencyDataReaderResponse(
                Timestamp.valueOf(LocalDateTime.now()),
                symbolName,
                BigDecimal.valueOf(1L));
    }
}
