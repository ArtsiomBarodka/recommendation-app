package com.crypto.app.service;

import com.crypto.app.model.response.CurrencyDataReaderResponse;
import org.springframework.lang.NonNull;

import java.util.List;

public interface CurrencyDataReader<T> {
    /**
     * Load Currency data from source.
     *
     * @param source - data source
     *
     * @return CurrencyDataReaderResponse
     */
    @NonNull
    List<CurrencyDataReaderResponse> read(@NonNull T source);
}
