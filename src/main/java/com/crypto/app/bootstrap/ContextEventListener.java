package com.crypto.app.bootstrap;

import com.crypto.app.config.PropertiesConfig;
import com.crypto.app.service.impl.CryptoCurrencyCSVReader;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class ContextEventListener {
    private final PropertiesConfig propertiesConfig;
    private final CryptoCurrencyCSVReader cryptoCurrencyCSVReader;

    @EventListener(ApplicationReadyEvent.class)
    public void afterApplicationAfterStartup() {
        if (propertiesConfig.isLoadCurrencyOnStartup()) {
            log.info("Start loading data on Startup");
            loadCsvData();
            log.info("Finish loading data on Startup");
        }
    }

    private void loadCsvData() {
        propertiesConfig.getCsvDataCurrencyFiles().forEach(fileName -> {
            log.info("Loading {} filename", fileName);
            cryptoCurrencyCSVReader.read(fileName).forEach(System.out::println);
        });
    }
}
