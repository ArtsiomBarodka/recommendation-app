package com.crypto.app.model.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.sql.Timestamp;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "currencies")
public class CurrencyEntity {
    @Id
    private Long id;

    @Column(name = "timestamp", nullable = false)
    private Timestamp timestamp;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "symbol_id")
    private SymbolEntity symbol;

    @Column(name = "price", nullable = false)
    private BigDecimal price;
}
