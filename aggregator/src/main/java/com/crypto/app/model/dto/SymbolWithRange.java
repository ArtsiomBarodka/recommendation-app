package com.crypto.app.model.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;

@Data
@EqualsAndHashCode(callSuper=true)
public class SymbolWithRange extends Symbol{
    private BigDecimal normalizedRange;
}
