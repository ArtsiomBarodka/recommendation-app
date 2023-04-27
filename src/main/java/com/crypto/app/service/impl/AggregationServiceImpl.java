package com.crypto.app.service.impl;

import com.crypto.app.exception.DataNotFoundException;
import com.crypto.app.model.dto.AggregationRule;
import com.crypto.app.model.dto.CryptoSymbolType;
import com.crypto.app.model.dto.TimeRange;
import com.crypto.app.model.entity.CryptoCurrencyEntity;
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
    public CryptoCurrencyEntity getAggregatedCurrency(CryptoSymbolType cryptoSymbolType, AggregationRule aggregationRule) {
        var aggregationResult = cryptoCurrencyRepository.findAllByCryptoSymbolName(cryptoSymbolType, aggregationRule.getPageRequest());

        if (aggregationResult.isEmpty()) {
            log.warn("Currency with (symbol = {}, aggregator = {}) is not found", cryptoSymbolType, aggregationRule);
            throw new DataNotFoundException("Currency with (symbol = %s, aggregator = %s) is not found".formatted(cryptoSymbolType, aggregationRule));
        }

        var result = aggregationResult.get(0);
        log.info("Currency with (symbol = {}, aggregator = {}) is found. Currency: {}", cryptoSymbolType, aggregationRule, result);

        return result;
    }

    @Override
    public CryptoCurrencyEntity getAggregatedCurrency(CryptoSymbolType cryptoSymbolType, AggregationRule aggregationRule, TimeRange timeRange) {
        var aggregationResult = cryptoCurrencyRepository.findAllByCryptoSymbolNameAndTimestampBetween(cryptoSymbolType, Timestamp.valueOf(timeRange.start()), Timestamp.valueOf(timeRange.end()), aggregationRule.getPageRequest());

        if (aggregationResult.isEmpty()) {
            log.warn("Currency with (symbol = {}, aggregator = {}, timeRange = {}) is not found", cryptoSymbolType, aggregationRule, timeRange);
            throw new DataNotFoundException("Currency with (symbol = %s, aggregator = %s) is not found".formatted(cryptoSymbolType, aggregationRule));
        }

        var result = aggregationResult.get(0);
        log.info("Currency with (symbol = {}, aggregator = {}, timeRange = {}) is found. Currency: {}", cryptoSymbolType, aggregationRule, timeRange, result);

        return result;
    }


}
