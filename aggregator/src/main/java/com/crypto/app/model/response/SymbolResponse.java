package com.crypto.app.model.response;

import com.crypto.app.model.SymbolType;
import lombok.Data;

@Data
public class SymbolResponse {
    private Long id;
    private SymbolType name;
}
