package com.crypto.app.repository;

import com.crypto.app.model.dto.CryptoSymbolType;
import com.crypto.app.model.entity.CryptoSymbolEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.lang.NonNull;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

public interface CryptoSymbolRepository extends JpaRepository<CryptoSymbolEntity, Long> {
    @NonNull
    Optional<CryptoSymbolEntity> findByName(@NonNull String name);

    @NonNull
    List<CryptoSymbolEntity> findAllByNameIn(@NonNull Collection<CryptoSymbolType> names);
}
