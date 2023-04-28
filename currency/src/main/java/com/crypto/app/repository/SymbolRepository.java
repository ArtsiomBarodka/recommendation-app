package com.crypto.app.repository;

import com.crypto.app.model.SymbolType;
import com.crypto.app.model.entity.SymbolEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.lang.NonNull;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

public interface SymbolRepository extends JpaRepository<SymbolEntity, Long> {
    @NonNull
    Optional<SymbolEntity> findByName(@NonNull String name);

    @NonNull
    List<SymbolEntity> findAllByNameIn(@NonNull Collection<SymbolType> names);
}
