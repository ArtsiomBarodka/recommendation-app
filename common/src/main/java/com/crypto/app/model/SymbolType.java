package com.crypto.app.model;

import java.util.Arrays;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

public enum SymbolType {
    BTC,
    DOGE,
    ETH,
    LTC,
    XRP;

    private static final Map<String, SymbolType> SYMBOLS = Arrays
            .stream(SymbolType.values())
            .collect(Collectors.toMap(Enum::name, Function.identity()));

    public static SymbolType of(String symbolName) {
        var key = symbolName != null ? symbolName.toUpperCase() : symbolName;
        return SYMBOLS.get(key);
    }
}
