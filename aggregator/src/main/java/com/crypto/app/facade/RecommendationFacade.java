package com.crypto.app.facade;

import com.crypto.app.model.dto.Currency;
import com.crypto.app.model.dto.SortMode;
import com.crypto.app.model.dto.SymbolWithRange;
import org.springframework.lang.NonNull;

import java.util.List;
import java.util.Optional;

public interface RecommendationFacade {
    /**
     *  Return Currency aggregated by aggregator.
     *  If period and date are not set calculate for the whole period.
     *  If date is not set calculate for the entire current period (Current Month, Day...).
     *
     * @param symbol - currency symbol
     * @param aggregator - aggregator for aggregating currency
     * @param period - optional: to find for specific timePeriod (Month, Day...)
     * @param date - optional: specific date
     *
     * @return Currency of symbol for aggregator
     */
    @NonNull
    Currency getCurrency(@NonNull String symbol, @NonNull String aggregator, @NonNull Optional<String> period, @NonNull Optional<String> date);

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
     *  Return symbol with the highest calculated normalised range for specific period.
     *  If date is not set calculate for current period (Current Year, Month, Day)
     *
     * @param period - specific timePeriod (Month, Day...)
     * @param date - optional: specific date
     *
     * @return Symbol with calculated normalised range
     */
    @NonNull
    SymbolWithRange getSymbolWithHighestNormalisedRange(@NonNull String period, @NonNull Optional<String> date);
}
