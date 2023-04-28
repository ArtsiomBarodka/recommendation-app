package com.crypto.app.service;

import com.crypto.app.model.dto.TimePeriod;
import com.crypto.app.model.dto.TimeRange;
import org.springframework.lang.NonNull;

public interface TimePeriodService {
    /**
     * Return calculate TimeRange for specific period and date.
     *
     * @param timePeriod - specific timePeriod (Month, Day...)
     * @param date - specific date
     *
     * @return TimeRange
     */
    @NonNull
    TimeRange getForSpecificDate(@NonNull TimePeriod timePeriod, @NonNull String date);

    /**
     * Return calculate TimeRange for current specific period.
     *
     * @param timePeriod - current specific timePeriod (Current Month, Day...)
     *
     * @return TimeRange
     */
    @NonNull
    TimeRange getForCurrentDate(@NonNull TimePeriod timePeriod);
}
