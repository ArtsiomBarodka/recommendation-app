package com.crypto.app.service;

import com.crypto.app.model.dto.Symbol;
import org.springframework.lang.NonNull;

public interface SymbolDBService {
    @NonNull Symbol getSymbolByName(@NonNull String name);
}
