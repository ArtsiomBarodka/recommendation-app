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

    /**
     *  Return Currency aggregated by aggregator.
     *  If period and date are not set calculate for the whole period.
     *  If date is not set calculate for the entire current period (Current Month, Day...).
     *
     *  Example 1:
     *  Request: /api/v1/crypto/recommendation/currencies/DOGE/PRICE_MIN?period=month&date=01-01-2022
     *  Response: {
     *   "id": 601,
     *   "timestamp": "2022-01-22 11:00:00",
     *   "symbol": {
     *     "id": 2,
     *     "name": "DOGE"
     *   },
     *   "price": 0.1290
     * }
     *
     * Example 2:
     * Request: /api/v1/crypto/recommendation/currencies/DOGE/PRICE_MAX
     * Response: {
     *   "id": 580,
     *   "timestamp": "2022-01-14 16:00:00",
     *   "symbol": {
     *     "id": 2,
     *     "name": "DOGE"
     *   },
     *   "price": 0.1941
     * }
     *
     * @param symbol - currency symbol
     * @param aggregator - aggregator for aggregating currency
     * @param period - optional: to find for specific timePeriod (Month, Day...)
     * @param date - optional: specific date (dd-MM-yyyy)
     *
     * @return CurrencyResponse of symbol for aggregator
     */
    @GetMapping("currencies/{symbol}/{aggregator}")
    public ResponseEntity<CurrencyResponse> getOldestCryptoCurrency(
            @PathVariable("symbol") String symbol,
            @PathVariable("aggregator") String aggregator,
            @RequestParam(value = "period", required = false) Optional<String> period,
            @RequestParam(value = "date", required = false) Optional<String> date) {

        var result = recommendationFacade.getCurrency(symbol, aggregator, period, date);

        return ResponseEntity.ok(toCurrencyResponse(result));
    }

    /**
     *  Return list of each symbol with calculated normalised range for the whole period.
     *  It is sorted by range depending on sort rule;
     *  If sort rule is not set sort by asc;
     *
     *  Example 1:
     *  Request: /api/v1/crypto/recommendation/symbols/with_range?sort=desc
     *  Response: [
     *   {
     *     "id": 3,
     *     "name": "ETH",
     *     "normalizedRange": 0.6384
     *   },
     *   {
     *     "id": 5,
     *     "name": "XRP",
     *     "normalizedRange": 0.5061
     *   },
     *   ...
     *   ]
     *
     *  Example 2:
     *  Request: /api/v1/crypto/recommendation/symbols/with_range
     *  Response: [
     *   {
     *     "id": 1,
     *     "name": "BTC",
     *     "normalizedRange": 0.4341
     *   },
     *   {
     *     "id": 4,
     *     "name": "LTC",
     *     "normalizedRange": 0.4652
     *   },
     *   ...
     *   ]
     *
     * @param sortBy - optional: direction of sort rule
     *
     * @return SymbolResponse list with calculated normalised range
     */
    @GetMapping("symbols/with_range")
    public ResponseEntity<List<SymbolWithRangeResponse>> getOldestCryptoCurrency(
            @RequestParam(value = "sort", required = false) String sortBy) {
        var allSymbolsSortedByNormalisedRange = recommendationFacade.getAllSymbolsWithNormalisedRange(SortMode.of(sortBy));

        return ResponseEntity.ok(
                allSymbolsSortedByNormalisedRange.stream()
                        .map(this::toSymbolWithRangeResponse)
                        .toList());
    }

    /**
     *  Return symbol with the highest calculated normalised range for specific period.
     *  If date is not set calculate for current period (Current Year, Month, Day)
     *
     * Example:
     * Request: /api/v1/crypto/recommendation/symbols/with_range/max?period=MONTH&date=02-01-2022
     * Response: {
     *   "id": 3,
     *   "name": "ETH",
     *   "normalizedRange": 0.6384
     * }
     *
     * @param period - specific timePeriod (Month, Day...)
     * @param date - optional: specific date
     *
     * @return SymbolResponse with calculated normalised range
     */
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
