package com.crypto.app.service;

import com.crypto.app.model.dto.AggregationRule;
import com.crypto.app.model.dto.CryptoSymbolType;
import com.crypto.app.model.dto.TimeRange;
import com.crypto.app.model.entity.CryptoCurrencyEntity;
import org.springframework.lang.NonNull;

public interface AggregationService {
    @NonNull
    CryptoCurrencyEntity getAggregatedCurrency(@NonNull CryptoSymbolType cryptoSymbolType, @NonNull AggregationRule aggregationRule);

    @NonNull
    CryptoCurrencyEntity getAggregatedCurrency(@NonNull CryptoSymbolType cryptoSymbolType, @NonNull AggregationRule aggregationRule, @NonNull TimeRange timeRange);
}
