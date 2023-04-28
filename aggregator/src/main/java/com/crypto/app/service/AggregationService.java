package com.crypto.app.service;

import com.crypto.app.model.dto.AggregationRule;
import com.crypto.app.model.dto.Currency;
import com.crypto.app.model.dto.SortMode;
import com.crypto.app.model.dto.Symbol;
import com.crypto.app.model.dto.SymbolWithRange;
import com.crypto.app.model.dto.TimeRange;
import org.springframework.lang.NonNull;

import java.util.List;

public interface AggregationService {
    /**
     *  Return Currency aggregated by aggregator for all period;
     *
     * @param symbol - currency symbol
     * @param aggregationRule - aggregator for aggregating currency
     *
     * @return Currency of symbol for aggregator
     */
    @NonNull
    Currency getCurrency(@NonNull Symbol symbol, @NonNull AggregationRule aggregationRule);

    /**
     *  Return Currency aggregated by aggregator for specific time range;
     *
     * @param symbol - currency symbol
     * @param aggregationRule - aggregator for aggregating currency
     * @param timeRange - time range
     *
     * @return Currency of symbol for aggregator
     */
    @NonNull
    Currency getCurrency(@NonNull Symbol symbol, @NonNull AggregationRule aggregationRule, @NonNull TimeRange timeRange);

    /**
     *  Return list of each symbol with calculated normalised range for the whole period.
     *  It is sorted by range depending on sort rule;
     *
     * @param sortMode - direction of sort rule
     *
     * @return Symbol list with calculated normalised range
     */
    @NonNull
    List<SymbolWithRange> getAllSymbolsWithNormalisedRange(@NonNull SortMode sortMode);

    /**
     *  Return symbol with the highest calculated normalised range for specific time range;
     *
     * @param timeRange - time range
     *
     * @return Symbol with calculated normalised range
     */
    @NonNull
    SymbolWithRange getSymbolWithHighestNormalisedRange(@NonNull TimeRange timeRange);
}
