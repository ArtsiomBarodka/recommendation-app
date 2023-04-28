package com.crypto.app.service;

import com.crypto.app.model.dto.Currency;
import com.crypto.app.model.dto.EventType;
import org.springframework.lang.NonNull;

public interface NotificationService {
    /**
     * Send notification to Aggregator microservice to sync data.
     *
     *
     * @param currency - new Currency item
     */
    void notifyAggregator(@NonNull Currency currency, @NonNull EventType eventType);
}
