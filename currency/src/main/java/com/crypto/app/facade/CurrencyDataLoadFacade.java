package com.crypto.app.facade;

import java.util.List;

public interface CurrencyDataLoadFacade {
    /**
     * Load Currency data to db from file.
     *
     * @param filePath - path to file
     */
    void loadDataFromFile(String filePath);

    /**
     * Load Currency data to db from files.
     *
     * @param filePath - path list to files
     */
    void loadDataFromFiles(List<String> filePath);
}
