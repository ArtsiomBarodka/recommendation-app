package com.crypto.app.model.response;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;

@Data
@EqualsAndHashCode(callSuper=true)
public class SymbolWithRangeResponse extends SymbolResponse {
    private BigDecimal normalizedRange;
}
