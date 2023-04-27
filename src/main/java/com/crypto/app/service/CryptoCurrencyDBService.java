package com.crypto.app.service;

import com.crypto.app.model.dto.CryptoCurrency;

import java.util.List;

public interface CryptoCurrencyDBService {
    void saveAll(List<CryptoCurrency> cryptoCurrencyList);
}
