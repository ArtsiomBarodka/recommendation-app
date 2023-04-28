package com.crypto.app.model.response;


import java.math.BigDecimal;
import java.sql.Timestamp;

public record CurrencyDataReaderResponse(Timestamp timestamp, String cryptoSymbol, BigDecimal price) {
}
