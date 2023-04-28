package com.crypto.app.service;

import com.crypto.app.model.dto.Currency;
import org.springframework.lang.NonNull;

public interface NotificationService {
    void notifyAggregator(@NonNull Currency currency);
}
