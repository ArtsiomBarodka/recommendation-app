package com.crypto.app.model.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.math.BigDecimal;
import java.sql.Timestamp;

@Data
public class CurrencyResponse {
    private Long id;

    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss")
    private Timestamp timestamp;

    private SymbolResponse symbol;

    private BigDecimal price;
}
