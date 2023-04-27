package com.crypto.app.model.dto;

import java.util.Arrays;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

public enum TimePeriod {
    CURRENT_DAY(true),
    CURRENT_MONTH(true),
    CURRENT_YEAR(true),
    DAY(false),
    MONTH(false),
    YEAR(false);

    private static final Map<String, TimePeriod> PERIODS = Arrays
            .stream(TimePeriod.values())
            .collect(Collectors.toMap(Enum::name, Function.identity()));

    public static TimePeriod of(String period) {
        var key = period != null ? period.toUpperCase() : period;
        return PERIODS.get(key);
    }

    private final boolean isCurrentPeriod;

    TimePeriod(boolean isCurrentPeriod) {
        this.isCurrentPeriod = isCurrentPeriod;
    }

    public boolean isCurrentPeriod() {
        return isCurrentPeriod;
    }

}
