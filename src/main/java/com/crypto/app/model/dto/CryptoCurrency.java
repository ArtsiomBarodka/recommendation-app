package com.crypto.app.model.dto;

import lombok.Data;

import java.math.BigDecimal;
import java.sql.Timestamp;

@Data
public class CryptoCurrency {
    private Long id;
    private Timestamp timestamp;
    private CryptoSymbol cryptoSymbol;
    private BigDecimal price;
}
