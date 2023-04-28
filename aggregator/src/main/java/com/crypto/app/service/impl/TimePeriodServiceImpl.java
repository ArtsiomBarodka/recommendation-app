package com.crypto.app.service.impl;

import com.crypto.app.model.dto.TimePeriod;
import com.crypto.app.model.dto.TimeRange;
import com.crypto.app.service.TimePeriodService;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Month;
import java.time.format.DateTimeFormatter;

@Service
public class TimePeriodServiceImpl implements TimePeriodService {
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("dd-MM-yyyy");

    @Override
    public TimeRange getForSpecificDate(TimePeriod timePeriod, @NonNull String date) {
        var requestedDate = LocalDate.parse(date, DATE_FORMATTER);

        return getTimeRange(timePeriod, requestedDate);
    }

    @Override
    public TimeRange getForCurrentDate(TimePeriod timePeriod) {
        var currentDate = LocalDate.now();

        return getTimeRange(timePeriod, currentDate);
    }

    private TimeRange getTimeRange(TimePeriod timePeriod, LocalDate requestedDate) {
        int requestedYear = requestedDate.getYear();
        var requestedMonth = requestedDate.getMonth();

        return switch (timePeriod) {
            case YEAR, CURRENT_YEAR -> {
                var start = LocalDateTime.of(requestedYear, Month.JANUARY, 1, 0, 0);
                var end = LocalDateTime.of(requestedYear, Month.DECEMBER, Month.DECEMBER.maxLength(), 23, 59, 59);
                yield new TimeRange(start, end);
            }
            case MONTH, CURRENT_MONTH -> {
                var start = LocalDateTime.of(requestedYear, requestedMonth, 1, 0, 0);
                var end = LocalDateTime.of(requestedYear, requestedMonth, requestedMonth.maxLength(), 23, 59, 59);
                yield new TimeRange(start, end);
            }
            case DAY, CURRENT_DAY -> {
                var start = LocalDateTime.of(requestedYear, requestedMonth, requestedDate.getDayOfMonth(), 0, 0);
                var end = LocalDateTime.of(requestedYear, requestedMonth, requestedDate.getDayOfMonth(), 23, 59, 59);
                yield new TimeRange(start, end);
            }
        };
    }
}
