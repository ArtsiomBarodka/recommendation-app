package com.crypto.app.model.response;


import java.math.BigDecimal;
import java.sql.Timestamp;

public record CryptoCurrencyDataReaderResponse(Timestamp timestamp, String cryptoSymbol, BigDecimal price) {
}
