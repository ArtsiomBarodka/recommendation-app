package com.crypto.app.service;

import com.crypto.app.model.dto.Currency;
import org.springframework.lang.NonNull;

import java.util.List;

public interface CurrencyDBService {
    /**
     * Safe and Return Currency list saved in db.
     *
     * @param currencyList - currency list which need to safe to db
     *
     * @return Currency list
     */
    @NonNull
    List<Currency> saveAll(List<Currency> currencyList);
}
