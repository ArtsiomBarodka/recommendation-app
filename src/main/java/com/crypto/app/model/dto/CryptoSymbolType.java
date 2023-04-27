package com.crypto.app.model.dto;

import java.util.Arrays;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

public enum CryptoSymbolType {
    BTC,
    DOGE,
    ETH,
    LTC,
    XRP;

    private static final Map<String, CryptoSymbolType> SYMBOLS = Arrays
            .stream(CryptoSymbolType.values())
            .collect(Collectors.toMap(Enum::name, Function.identity()));

    public static CryptoSymbolType of(String symbolName) {
        var key = symbolName != null ? symbolName.toUpperCase() : symbolName;
        return SYMBOLS.get(key);
    }
}
