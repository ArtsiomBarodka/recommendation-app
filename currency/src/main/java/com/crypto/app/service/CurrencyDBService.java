package com.crypto.app.service;

import com.crypto.app.model.dto.Currency;
import org.springframework.lang.NonNull;

import java.util.List;

public interface CurrencyDBService {
    /**
     *
     * @param currencyList
     * @return
     */
    @NonNull
    List<Currency> saveAll(List<Currency> currencyList);
}
