package com.crypto.app.model.entity;

import com.crypto.app.model.SymbolType;
import com.crypto.app.model.dto.EventType;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "events")
public class CurrencyEventEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(name = "event", nullable = false)
    private EventType eventType;

    @CreationTimestamp
    @Column(name = "created", nullable = false)
    private LocalDateTime created;

    @OneToMany(cascade = CascadeType.PERSIST)
    @JoinColumn(name = "event_id")
    private List<CurrencyEntity> currencyEntityList;
}
