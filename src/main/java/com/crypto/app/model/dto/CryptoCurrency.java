package com.crypto.app.model.dto;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class CryptoCurrency {
    private Long id;
    private CryptoSymbol cryptoSymbol;
    private BigDecimal price;
}
