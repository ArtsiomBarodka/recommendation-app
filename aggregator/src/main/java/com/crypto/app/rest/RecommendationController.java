package com.crypto.app.rest;

import com.crypto.app.facade.RecommendationFacade;
import com.crypto.app.model.dto.Currency;
import com.crypto.app.model.dto.SortMode;
import com.crypto.app.model.dto.SymbolWithRange;
import com.crypto.app.model.response.CurrencyResponse;
import com.crypto.app.model.response.SymbolResponse;
import com.crypto.app.model.response.SymbolWithRangeResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/v1/crypto/recommendation")
@RequiredArgsConstructor
public class RecommendationController {
    private final RecommendationFacade recommendationFacade;

    @GetMapping("currencies/{symbol}/{aggregator}")
    public ResponseEntity<CurrencyResponse> getOldestCryptoCurrency(
            @PathVariable("symbol") String symbol,
            @PathVariable("aggregator") String aggregator,
            @RequestParam(value = "period", required = false) Optional<String> period,
            @RequestParam(value = "date", required = false) Optional<String> date) {

        var result = recommendationFacade.getCurrency(symbol, aggregator, period, date);

        return ResponseEntity.ok(toCurrencyResponse(result));
    }

    @GetMapping("symbols/with_range")
    public ResponseEntity<List<SymbolWithRangeResponse>> getOldestCryptoCurrency(
            @RequestParam(value = "sort", required = false) String sortBy) {
        var allSymbolsSortedByNormalisedRange = recommendationFacade.getAllSymbolsWithNormalisedRange(SortMode.of(sortBy));

        return ResponseEntity.ok(
                allSymbolsSortedByNormalisedRange.stream()
                        .map(this::toSymbolWithRangeResponse)
                        .toList());
    }

    @GetMapping("symbols/with_range/max")
    public ResponseEntity<SymbolWithRangeResponse> getOldestCryptoCurrency(
            @RequestParam(value = "period") String period,
            @RequestParam(value = "date", required = false) Optional<String> date) {

        var result = recommendationFacade.getSymbolWithHighestNormalisedRange(period, date);
        return ResponseEntity.ok(toSymbolWithRangeResponse(result));
    }

    private CurrencyResponse toCurrencyResponse(Currency source) {
        var symbolResponse = new SymbolResponse();
        symbolResponse.setId(source.getSymbol().getId());
        symbolResponse.setName(source.getSymbol().getName());

        var currencyResponse = new CurrencyResponse();
        currencyResponse.setId(source.getId());
        currencyResponse.setTimestamp(source.getTimestamp());
        currencyResponse.setPrice(source.getPrice());
        currencyResponse.setSymbol(symbolResponse);

        return currencyResponse;
    }

    private SymbolWithRangeResponse toSymbolWithRangeResponse(SymbolWithRange source) {
        var symbolResponse = new SymbolWithRangeResponse();
        symbolResponse.setId(source.getId());
        symbolResponse.setName(source.getName());
        symbolResponse.setNormalizedRange(source.getNormalizedRange());

        return symbolResponse;
    }
}
