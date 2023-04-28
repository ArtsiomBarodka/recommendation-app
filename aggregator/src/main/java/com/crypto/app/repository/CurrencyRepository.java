package com.crypto.app.repository;

import com.crypto.app.model.SymbolType;
import com.crypto.app.model.entity.CurrencyEntity;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.sql.Timestamp;
import java.util.List;

public interface CurrencyRepository extends JpaRepository<CurrencyEntity, Long> {
    @EntityGraph(attributePaths = "symbol")
    List<CurrencyEntity> findAllBySymbolNameAndTimestampBetween(SymbolType name, Timestamp startDate, Timestamp endDate, PageRequest pageRequest);

    @EntityGraph(attributePaths = "symbol")
    List<CurrencyEntity> findAllBySymbolName(SymbolType name, PageRequest pageRequest);

}
