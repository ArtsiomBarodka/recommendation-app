package com.crypto.app.service;

import com.crypto.app.model.SymbolType;
import com.crypto.app.model.dto.AggregationRule;
import com.crypto.app.model.dto.Currency;
import com.crypto.app.model.dto.SortMode;
import com.crypto.app.model.dto.SymbolWithRange;
import com.crypto.app.model.dto.TimeRange;
import org.springframework.lang.NonNull;

import java.util.List;

public interface AggregationService {
    @NonNull
    Currency getCurrency(@NonNull SymbolType symbolType, @NonNull AggregationRule aggregationRule);

    @NonNull
    Currency getCurrency(@NonNull SymbolType symbolType, @NonNull AggregationRule aggregationRule, @NonNull TimeRange timeRange);

    @NonNull
    List<SymbolWithRange> getAllSymbolsWithNormalisedRange(@NonNull SortMode sortMode);

    @NonNull
    SymbolWithRange getSymbolWithHighestNormalisedRange(@NonNull TimeRange timeRange);
}
