package com.crypto.app.model.dto;

import com.crypto.app.model.SymbolType;
import lombok.Data;

@Data
public class Symbol {
    private Long id;
    private SymbolType name;
}
