package com.crypto.app.service;

import com.crypto.app.model.dto.TimePeriod;
import com.crypto.app.model.dto.TimeRange;
import org.springframework.lang.NonNull;

public interface TimePeriodService {
    @NonNull
    TimeRange getForSpecificDate(@NonNull TimePeriod timePeriod, @NonNull String date);

    @NonNull
    TimeRange getForCurrentDate(@NonNull TimePeriod timePeriod);
}
