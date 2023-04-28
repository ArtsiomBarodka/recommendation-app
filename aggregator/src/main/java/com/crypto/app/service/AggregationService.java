package com.crypto.app.service;

import com.crypto.app.model.SymbolType;
import com.crypto.app.model.dto.AggregationRule;
import com.crypto.app.model.dto.Currency;
import com.crypto.app.model.dto.TimeRange;
import org.springframework.lang.NonNull;

public interface AggregationService {
    @NonNull
    Currency getAggregatedCurrency(@NonNull SymbolType symbolType, @NonNull AggregationRule aggregationRule);

    @NonNull
    Currency getAggregatedCurrency(@NonNull SymbolType symbolType, @NonNull AggregationRule aggregationRule, @NonNull TimeRange timeRange);
}
