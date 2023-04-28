package com.crypto.app.model.message;

import com.crypto.app.model.SymbolType;
import lombok.Data;

@Data
public class SymbolMessageItem {
    private Long id;
    private SymbolType name;
}
