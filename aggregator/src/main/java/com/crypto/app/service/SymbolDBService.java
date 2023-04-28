package com.crypto.app.service;

import com.crypto.app.model.dto.Symbol;
import org.springframework.lang.NonNull;

public interface SymbolDBService {
    /**
     * Return Symbol from db by symbol name.
     *
     * @param name - symbol name
     *
     * @return Symbol
     */
    @NonNull Symbol getSymbolByName(@NonNull String name);
}
