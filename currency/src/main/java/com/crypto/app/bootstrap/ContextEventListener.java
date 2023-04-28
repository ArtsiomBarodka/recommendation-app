package com.crypto.app.bootstrap;

import com.crypto.app.config.PropertiesConfig;
import com.crypto.app.facade.CurrencyDataLoadFacade;
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
    private final CurrencyDataLoadFacade currencyDataLoadFacade;

    @EventListener(ApplicationReadyEvent.class)
    public void afterApplicationStartup() {
        if (propertiesConfig.isLoadCurrencyOnStartup()) {
            log.info("Start loading data on Startup");
            currencyDataLoadFacade.loadDataFromFile(propertiesConfig.getCsvDataCurrencyFiles());
            log.info("Finish loading data on Startup");
        }
    }
}
