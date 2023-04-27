package com.crypto.app.repository;

import com.crypto.app.model.dto.CryptoSymbolType;
import com.crypto.app.model.entity.CryptoCurrencyEntity;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;

import java.sql.Timestamp;
import java.util.List;

public interface CryptoCurrencyRepository extends JpaRepository<CryptoCurrencyEntity, Long> {
    List<CryptoCurrencyEntity> findAllByCryptoSymbolNameAndTimestampBetween(CryptoSymbolType name, Timestamp startDate, Timestamp endDate, PageRequest pageRequest);

    List<CryptoCurrencyEntity> findAllByCryptoSymbolName(CryptoSymbolType name, PageRequest pageRequest);
}
