package com.crypto.app.model.dto;

import lombok.Data;

import java.math.BigDecimal;
import java.sql.Timestamp;

@Data
public class Currency {
    private Long id;
    private Timestamp timestamp;
    private Symbol symbol;
    private BigDecimal price;
}
