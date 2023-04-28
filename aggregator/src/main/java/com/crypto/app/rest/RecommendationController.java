package com.crypto.app.rest;

import com.crypto.app.model.SymbolType;
import com.crypto.app.model.dto.AggregationRule;
import com.crypto.app.model.dto.Currency;
import com.crypto.app.model.dto.SortMode;
import com.crypto.app.model.dto.SymbolWithRange;
import com.crypto.app.model.dto.TimePeriod;
import com.crypto.app.service.AggregationService;
import com.crypto.app.service.TimePeriodService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/crypto/recommendation")
@RequiredArgsConstructor
public class RecommendationController {
    private final AggregationService aggregationService;
    private final TimePeriodService timePeriodService;

    @GetMapping("currencies/{symbol}/{aggregator}")
    public ResponseEntity<Currency> getOldestCryptoCurrency(
            @PathVariable("symbol") String symbol,
            @PathVariable("aggregator") String aggregator,
            @RequestParam(value = "period", required = false) String period,
            @RequestParam(value = "date", required = false) String date) {

        var timePeriod = TimePeriod.of(period);
        var rule = AggregationRule.of(aggregator);
        var cryptoSymbol = SymbolType.of(symbol);

        if (timePeriod != null && (timePeriod.isCurrentPeriod() || date != null)) {
            var timeRange = timePeriod.isCurrentPeriod() ? timePeriodService.getForCurrentDate(timePeriod) : timePeriodService.getForSpecificDate(timePeriod, date);
            Currency aggregatedCurrency = aggregationService.getAggregatedCurrency(cryptoSymbol, rule, timeRange);
            return ResponseEntity.ok(aggregatedCurrency);
        } else {
            Currency aggregatedCurrency = aggregationService.getAggregatedCurrency(cryptoSymbol, rule);
            return ResponseEntity.ok(aggregatedCurrency);
        }
    }

    @GetMapping("symbols/with_range")
    public ResponseEntity<List<SymbolWithRange>> getOldestCryptoCurrency(
            @RequestParam(value = "sort", required = false) String sortBy) {
        var allSymbolsSortedByNormalisedRange = aggregationService.getAllSymbolsWithNormalisedRangeSortedBy(SortMode.of(sortBy));


        return ResponseEntity.ok(allSymbolsSortedByNormalisedRange);
    }
}
