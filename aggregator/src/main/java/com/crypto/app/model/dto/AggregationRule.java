package com.crypto.app.model.dto;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;

import java.util.Arrays;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

public enum AggregationRule {
    PRICE_MAX {
        public PageRequest getPageRequest() {
            return PageRequest.of(0, 1, Sort.by(Sort.Direction.DESC, "price"));
        }
    },

    PRICE_MIN {
        public PageRequest getPageRequest() {
            return PageRequest.of(0, 1, Sort.by(Sort.Direction.ASC, "price"));
        }
    },

    NEWEST {
        public PageRequest getPageRequest() {
            return PageRequest.of(0, 1, Sort.by(Sort.Direction.DESC, "timestamp"));
        }
    },

    OLDEST {
        public PageRequest getPageRequest() {
            return PageRequest.of(0, 1, Sort.by(Sort.Direction.ASC, "timestamp"));
        }
    };

    private static final Map<String, AggregationRule> RULES = Arrays
            .stream(AggregationRule.values())
            .collect(Collectors.toMap(Enum::name, Function.identity()));

    public abstract PageRequest getPageRequest();

    public static AggregationRule of(String aggregator) {
        var key = aggregator != null ? aggregator.toUpperCase() : aggregator;
        return RULES.get(key);
    }
}
