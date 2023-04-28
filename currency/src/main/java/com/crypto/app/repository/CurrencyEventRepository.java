package com.crypto.app.repository;

import com.crypto.app.model.entity.CurrencyEventEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CurrencyEventRepository extends JpaRepository<CurrencyEventEntity, Long> {
}
