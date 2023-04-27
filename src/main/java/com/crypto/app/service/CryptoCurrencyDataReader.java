package com.crypto.app.service;

import com.crypto.app.model.response.CryptoCurrencyDataReaderResponse;
import org.springframework.lang.NonNull;

import java.util.List;

public interface CryptoCurrencyDataReader<T> {
    @NonNull
    List<CryptoCurrencyDataReaderResponse> read(@NonNull T source);
}