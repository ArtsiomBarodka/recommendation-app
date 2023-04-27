package com.crypto.app.facade;

import java.util.List;

public interface CryptoCurrencyDataLoadFacade {
    void loadDataFromCSV(String filePath);
    void loadDataFromCSV(List<String> filePath);
}
