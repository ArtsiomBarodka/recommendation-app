package com.crypto.app.facade;

import java.util.List;

public interface CurrencyDataLoadFacade {
    void loadDataFromFile(String filePath);
    void loadDataFromFiles(List<String> filePath);
}
