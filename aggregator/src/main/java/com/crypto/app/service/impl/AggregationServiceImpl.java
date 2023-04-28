package com.crypto.app.service.impl;

import com.crypto.app.exception.DataNotFoundException;
import com.crypto.app.model.SymbolType;
import com.crypto.app.model.dto.AggregationRule;
import com.crypto.app.model.dto.Currency;
import com.crypto.app.model.dto.Symbol;
import com.crypto.app.model.dto.TimeRange;
import com.crypto.app.model.entity.CurrencyEntity;
import com.crypto.app.repository.CryptoCurrencyRepository;
import com.crypto.app.service.AggregationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;

@Slf4j
@Service
@RequiredArgsConstructor
public class AggregationServiceImpl implements AggregationService {
    private final CryptoCurrencyRepository cryptoCurrencyRepository;

    @Override
    public Currency getAggregatedCurrency(SymbolType symbolType, AggregationRule aggregationRule) {
        var aggregationResult = cryptoCurrencyRepository.findAllBySymbolName(symbolType, aggregationRule.getPageRequest());

        if (aggregationResult.isEmpty()) {
            log.warn("Currency with (symbol = {}, aggregator = {}) is not found", symbolType, aggregationRule);
            throw new DataNotFoundException("Currency with (symbol = %s, aggregator = %s) is not found".formatted(symbolType, aggregationRule));
        }

        var result = aggregationResult.get(0);
        log.info("Currency with (symbol = {}, aggregator = {}) is found. Currency: {}", symbolType, aggregationRule, result);

        return toCurrencyDto(result);
    }

    @Override
    public Currency getAggregatedCurrency(SymbolType symbolType, AggregationRule aggregationRule, TimeRange timeRange) {
        var aggregationResult = cryptoCurrencyRepository.findAllBySymbolNameAndTimestampBetween(symbolType, Timestamp.valueOf(timeRange.start()), Timestamp.valueOf(timeRange.end()), aggregationRule.getPageRequest());

        if (aggregationResult.isEmpty()) {
            log.warn("Currency with (symbol = {}, aggregator = {}, timeRange = {}) is not found", symbolType, aggregationRule, timeRange);
            throw new DataNotFoundException("Currency with (symbol = %s, aggregator = %s) is not found".formatted(symbolType, aggregationRule));
        }

        var result = aggregationResult.get(0);
        log.info("Currency with (symbol = {}, aggregator = {}, timeRange = {}) is found. Currency: {}", symbolType, aggregationRule, timeRange, result);

        return toCurrencyDto(result);
    }

    private Currency toCurrencyDto(CurrencyEntity source) {
        var symbol = new Symbol();
        symbol.setId(source.getSymbol().getId());
        symbol.setName(source.getSymbol().getName());

        var currency = new Currency();
        currency.setId(source.getId());
        currency.setTimestamp(source.getTimestamp());
        currency.setPrice(source.getPrice());
        currency.setSymbol(symbol);

        return currency;
    }
}
