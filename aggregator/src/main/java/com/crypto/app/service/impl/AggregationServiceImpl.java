package com.crypto.app.service.impl;

import com.crypto.app.exception.DataNotFoundException;
import com.crypto.app.model.SymbolType;
import com.crypto.app.model.dto.AggregationRule;
import com.crypto.app.model.dto.Currency;
import com.crypto.app.model.dto.SortMode;
import com.crypto.app.model.dto.Symbol;
import com.crypto.app.model.dto.SymbolWithRange;
import com.crypto.app.model.dto.TimeRange;
import com.crypto.app.model.entity.CurrencyEntity;
import com.crypto.app.model.entity.SymbolEntity;
import com.crypto.app.repository.CurrencyRepository;
import com.crypto.app.repository.SymbolRepository;
import com.crypto.app.service.AggregationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Comparator;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class AggregationServiceImpl implements AggregationService {
    private static final Comparator<SymbolWithRange> ASC_SYMBOL_WITH_RANGE_COMPARATOR = Comparator.comparing(SymbolWithRange::getNormalizedRange);

    private static final Comparator<SymbolWithRange> DESC_SYMBOL_WITH_RANGE_COMPARATOR = Comparator.comparing(SymbolWithRange::getNormalizedRange).reversed();

    private final CurrencyRepository currencyRepository;
    private final SymbolRepository symbolRepository;

    @Override
    public Currency getCurrency(SymbolType symbolType, AggregationRule aggregationRule) {
        var aggregationResult = currencyRepository.findAllBySymbolName(symbolType, aggregationRule.getPageRequest());

        if (aggregationResult.isEmpty()) {
            log.warn("Currency with (symbol = {}, aggregator = {}) is not found", symbolType, aggregationRule);
            throw new DataNotFoundException("Currency with (symbol = %s, aggregator = %s) is not found".formatted(symbolType, aggregationRule));
        }

        var result = aggregationResult.get(0);
        log.info("Currency with (symbol = {}, aggregator = {}) is found. Currency: {}", symbolType, aggregationRule, result);

        return toCurrencyDto(result);
    }

    @Override
    public Currency getCurrency(SymbolType symbolType, AggregationRule aggregationRule, TimeRange timeRange) {
        var aggregationResult = currencyRepository.findAllBySymbolNameAndTimestampBetween(symbolType, timeRange.start(), timeRange.end(), aggregationRule.getPageRequest());

        if (aggregationResult.isEmpty()) {
            log.warn("Currency with (symbol = {}, aggregator = {}, timeRange = {}) is not found", symbolType, aggregationRule, timeRange);
            throw new DataNotFoundException("Currency with (symbol = %s, aggregator = %s, timeRange = %s) is not found".formatted(symbolType, aggregationRule, timeRange));
        }

        var result = aggregationResult.get(0);
        log.info("Currency with (symbol = {}, aggregator = {}, timeRange = {}) is found. Currency: {}", symbolType, aggregationRule, timeRange, result);

        return toCurrencyDto(result);
    }

    @Override
    public SymbolWithRange getSymbolWithHighestNormalisedRange(TimeRange timeRange) {
        var allSymbols = symbolRepository.findAll();

        var symbolWithMaxRange = allSymbols.parallelStream().map(symbol -> {
                    var maxPriceCurrency = currencyRepository.findAllBySymbolNameAndTimestampBetween(symbol.getName(), timeRange.start(), timeRange.end(), AggregationRule.PRICE_MAX.getPageRequest());
                    if (maxPriceCurrency.isEmpty()) {
                        log.warn("Currency with (symbol = {}, aggregator = {}) for time range = {} is not found ", symbol, AggregationRule.PRICE_MAX, timeRange);
                        throw new DataNotFoundException("Currency with (symbol = %s, aggregator = %s) for time range = %s is not found".formatted(symbol, AggregationRule.PRICE_MAX, timeRange));
                    }

                    var minPriceCurrency = currencyRepository.findAllBySymbolName(symbol.getName(), AggregationRule.PRICE_MIN.getPageRequest());
                    if (minPriceCurrency.isEmpty()) {
                        log.warn("Currency with (symbol = {}, aggregator = {}) for time range = {} is not found", symbol, AggregationRule.PRICE_MIN, timeRange);
                        throw new DataNotFoundException("Currency with (symbol = %s, aggregator = %s) for time range = %s is not found".formatted(symbol, AggregationRule.PRICE_MIN, timeRange));
                    }

                    var normalizedRange = calculateNormalizedRange(maxPriceCurrency, minPriceCurrency);

                    var symbolWithRange = toSymbolWithRangeDto(symbol, normalizedRange);
                    log.info("Symbol with normalized range is found. Symbol: {}", symbolWithRange);

                    return symbolWithRange;
                })
                .max(ASC_SYMBOL_WITH_RANGE_COMPARATOR)
                .orElseThrow(() -> {
                    log.warn("Symbol with highest normalized range is not found.");
                    throw new DataNotFoundException("Symbol with highest normalized range is not found.");
                });

        log.info("Symbol with highest normalized range is found. Symbol: {}", symbolWithMaxRange);

        return symbolWithMaxRange;
    }

    @Override
    public List<SymbolWithRange> getAllSymbolsWithNormalisedRange(@NonNull SortMode sortMode) {
        var allSymbols = symbolRepository.findAll();
        var comparing = SortMode.ASC == sortMode ? ASC_SYMBOL_WITH_RANGE_COMPARATOR : DESC_SYMBOL_WITH_RANGE_COMPARATOR;

        return allSymbols.parallelStream().map(symbol -> {
            var maxPriceCurrency = currencyRepository.findAllBySymbolName(symbol.getName(), AggregationRule.PRICE_MAX.getPageRequest());
            if (maxPriceCurrency.isEmpty()) {
                log.warn("Currency with (symbol = {}, aggregator = {}) is not found", symbol, AggregationRule.PRICE_MAX);
                throw new DataNotFoundException("Currency with (symbol = %s, aggregator = %s) is not found".formatted(symbol, AggregationRule.PRICE_MAX));
            }

            var minPriceCurrency = currencyRepository.findAllBySymbolName(symbol.getName(), AggregationRule.PRICE_MIN.getPageRequest());
            if (minPriceCurrency.isEmpty()) {
                log.warn("Currency with (symbol = {}, aggregator = {}) is not found", symbol, AggregationRule.PRICE_MIN);
                throw new DataNotFoundException("Currency with (symbol = %s, aggregator = %s) is not found".formatted(symbol, AggregationRule.PRICE_MIN));
            }

            var normalizedRange = calculateNormalizedRange(maxPriceCurrency, minPriceCurrency);

            var symbolWithRange = toSymbolWithRangeDto(symbol, normalizedRange);
            log.info("Symbol with normalized range is found. Symbol: {}", symbolWithRange);

            return symbolWithRange;
        }).sorted(comparing).toList();
    }

    private SymbolWithRange toSymbolWithRangeDto(SymbolEntity source, BigDecimal normalizedRange) {
        var symbolWithRange = new SymbolWithRange();
        symbolWithRange.setId(source.getId());
        symbolWithRange.setName(source.getName());
        symbolWithRange.setNormalizedRange(normalizedRange);

        return symbolWithRange;
    }

    private BigDecimal calculateNormalizedRange(List<CurrencyEntity> maxPriceCurrency, List<CurrencyEntity> minPriceCurrency) {
        var maxPrice = maxPriceCurrency.get(0).getPrice();
        var minPrice = minPriceCurrency.get(0).getPrice();

        return maxPrice.subtract(minPrice).divide(minPrice, 4, RoundingMode.HALF_UP);
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
