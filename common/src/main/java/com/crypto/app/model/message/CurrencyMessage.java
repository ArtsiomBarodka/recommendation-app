package com.crypto.app.model.message;

import lombok.Data;

import java.math.BigDecimal;
import java.sql.Timestamp;

@Data
public class CurrencyMessage {
    private Long id;
    private Timestamp timestamp;
    private SymbolMessageItem cryptoSymbol;
    private BigDecimal price;
}
