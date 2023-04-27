package com.crypto.app.config;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.List;

@Getter
@Component
public class PropertiesConfig {
    @Value("${crypto.recommendation.app.currency.load-data-on-startup}")
    private boolean loadCurrencyOnStartup;
    @Value("${crypto.recommendation.app.currency.data.csv}")
    private List<String> csvDataCurrencyFiles;
}
