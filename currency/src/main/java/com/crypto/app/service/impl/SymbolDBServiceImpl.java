package com.crypto.app.service.impl;

import com.crypto.app.exception.DataNotFoundException;
import com.crypto.app.model.dto.Symbol;
import com.crypto.app.model.entity.SymbolEntity;
import com.crypto.app.repository.SymbolRepository;
import com.crypto.app.service.SymbolDBService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class SymbolDBServiceImpl implements SymbolDBService {
    private final SymbolRepository symbolRepository;

    @Override
    public Symbol getSymbolByName(String name) {
        var symbolEntity = symbolRepository.findByName(name)
                .orElseThrow(() -> {
                    log.warn("Symbol with (name = {}) is not found", name);
                    return new DataNotFoundException("Symbol with (name = %s) is not found".formatted(name));
                });

        log.info("Symbol with (name = {}) is found. Symbol: {}", name, symbolEntity);

        return toDto(symbolEntity);
    }

    private Symbol toDto(SymbolEntity source) {
        var symbol = new Symbol();
        symbol.setId(source.getId());
        symbol.setName(source.getName());

        return symbol;
    }
}
