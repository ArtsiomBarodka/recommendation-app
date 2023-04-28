package com.crypto.app.repository;

import com.crypto.app.model.SymbolType;
import com.crypto.app.model.entity.CurrencyEntity;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;

import java.sql.Timestamp;
import java.util.List;

public interface CurrencyRepository extends JpaRepository<CurrencyEntity, Long> {
    List<CurrencyEntity> findAllBySymbolNameAndTimestampBetween(SymbolType name, Timestamp startDate, Timestamp endDate, PageRequest pageRequest);

    List<CurrencyEntity> findAllBySymbolName(SymbolType name, PageRequest pageRequest);

//    @Query("SELECT c FROM CurrencyEntity c WHERE c.symbol.id = :cryptoId HAVING MIN(c.price) ")
//    BigDecimal findMinValue()
}
