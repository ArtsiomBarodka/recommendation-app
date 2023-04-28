package com.crypto.app.facade.impl;

import com.crypto.app.facade.RecommendationFacade;
import com.crypto.app.model.dto.AggregationRule;
import com.crypto.app.model.dto.Currency;
import com.crypto.app.model.dto.SortMode;
import com.crypto.app.model.dto.SymbolWithRange;
import com.crypto.app.model.dto.TimePeriod;
import com.crypto.app.service.AggregationService;
import com.crypto.app.service.SymbolDBService;
import com.crypto.app.service.TimePeriodService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class RecommendationFacadeImpl implements RecommendationFacade {
    private final AggregationService aggregationService;
    private final TimePeriodService timePeriodService;
    private final SymbolDBService symbolDBService;

    @Override
    public Currency getCurrency(String symbol, String aggregator, Optional<String> period, Optional<String> date) {
        var rule = AggregationRule.of(aggregator);
        var cryptoSymbol = symbolDBService.getSymbolByName(symbol);

        if (period.isPresent()) {
            TimePeriod timePeriod = TimePeriod.of(period.get());
            if (timePeriod != null && (timePeriod.isCurrentPeriod() || date.isPresent())) {
                var timeRange = timePeriod.isCurrentPeriod() ?
                        timePeriodService.getForCurrentDate(timePeriod) :
                        timePeriodService.getForSpecificDate(timePeriod, date.get());

                return aggregationService.getCurrency(cryptoSymbol, rule, timeRange);
            }
        }

        return aggregationService.getCurrency(cryptoSymbol, rule);
    }

    @Override
    public List<SymbolWithRange> getAllSymbolsWithNormalisedRange(SortMode sortMode) {
        return aggregationService.getAllSymbolsWithNormalisedRange(sortMode);
    }

    @Override
    public SymbolWithRange getSymbolWithHighestNormalisedRange(String period, Optional<String> date) {
        var timePeriod = TimePeriod.of(period);
        var timeRange = (timePeriod.isCurrentPeriod() || date.isEmpty()) ?
                timePeriodService.getForCurrentDate(timePeriod) :
                timePeriodService.getForSpecificDate(timePeriod, date.get());

        return aggregationService.getSymbolWithHighestNormalisedRange(timeRange);
    }
}
