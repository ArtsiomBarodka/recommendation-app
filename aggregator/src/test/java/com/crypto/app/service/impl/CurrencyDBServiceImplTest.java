package com.crypto.app.service.impl;

import com.crypto.app.exception.DataNotFoundException;
import com.crypto.app.model.dto.Currency;
import com.crypto.app.model.dto.Symbol;
import com.crypto.app.model.entity.CurrencyEntity;
import com.crypto.app.model.entity.SymbolEntity;
import com.crypto.app.repository.CurrencyRepository;
import com.crypto.app.repository.SymbolRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class CurrencyDBServiceImplTest {
    @Mock
    private CurrencyRepository currencyRepository;

    @Mock
    private SymbolRepository symbolRepository;

    @InjectMocks
    private CurrencyDBServiceImpl currencyDBService;

    @Test
    void saveAllTest() {
        var symbolName = "name";
        var currency = createCurrency(symbolName);

        var resultSymbolEntity = new SymbolEntity(1L, symbolName);
        var resultCurrencyEntity = new CurrencyEntity(1L, Timestamp.valueOf(LocalDateTime.now()), resultSymbolEntity, BigDecimal.valueOf(10));

        when(symbolRepository.findAllByNameIn(any())).thenReturn(List.of(resultSymbolEntity));
        when(currencyRepository.saveAll(any())).thenReturn(List.of(resultCurrencyEntity));

        var resultList = currencyDBService.saveAll(List.of(currency));

        assertNotNull(resultList);
        assertEquals(1, resultList.size());

        var resultItem = resultList.get(0);
        assertEquals(resultCurrencyEntity.getId(), resultItem.getId());
        assertEquals(resultCurrencyEntity.getPrice(), resultItem.getPrice());
        assertEquals(resultCurrencyEntity.getTimestamp().getTime(), resultItem.getTimestamp().getTime());
        assertEquals(resultCurrencyEntity.getSymbol().getId(), resultItem.getSymbol().getId());
        assertEquals(resultCurrencyEntity.getSymbol().getName(), resultItem.getSymbol().getName());
    }

    @Test
    void saveAllTest_symbolIsNotExist() {
        var symbolName = "name";
        var currency = createCurrency(symbolName);

        when(symbolRepository.findAllByNameIn(any())).thenReturn(List.of(new SymbolEntity(1L, "anotherName")));

        assertThrows(DataNotFoundException.class, () -> currencyDBService.saveAll(List.of(currency)));
    }

    private Currency createCurrency(String symbolName) {
        var symbol = new Symbol();
        symbol.setId(1L);
        symbol.setName(symbolName);

        var currency = new Currency();
        currency.setPrice(BigDecimal.valueOf(10));
        currency.setSymbol(symbol);
        currency.setTimestamp(Timestamp.valueOf(LocalDateTime.now()));
        return currency;
    }
}
