package com.crypto.app.facade;

import com.crypto.app.model.dto.Currency;
import com.crypto.app.model.dto.SortMode;
import com.crypto.app.model.dto.SymbolWithRange;
import org.springframework.lang.NonNull;

import java.util.List;
import java.util.Optional;

public interface RecommendationFacade {
    @NonNull
    Currency getCurrency(@NonNull String symbol, @NonNull String aggregator, @NonNull Optional<String> period, @NonNull Optional<String> date);

    @NonNull
    List<SymbolWithRange> getAllSymbolsWithNormalisedRange(@NonNull SortMode sortMode);

    @NonNull
    SymbolWithRange getSymbolWithHighestNormalisedRange(@NonNull String period, @NonNull Optional<String> date);
}
