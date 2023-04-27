package com.crypto.app.service.impl;

import com.crypto.app.exception.DataReaderException;
import com.crypto.app.model.response.CryptoCurrencyDataReaderResponse;
import com.crypto.app.service.CryptoCurrencyDataReader;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.io.ClassPathResource;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.math.BigDecimal;
import java.nio.file.Files;
import java.nio.file.Path;
import java.sql.Timestamp;
import java.util.Collections;
import java.util.List;

@Slf4j
@Qualifier("CSV")
@Service
public class CryptoCurrencyCSVDataReader implements CryptoCurrencyDataReader<String> {
    private static final Integer ATTRIBUTES_LENGTH = 3;
    private static final String COMMA_DELIMITER = ",";

    @Override
    public @NonNull List<CryptoCurrencyDataReaderResponse> read(@NonNull String path) {
        List<CryptoCurrencyDataReaderResponse> cryptoCurrencyList = Collections.emptyList();

        try (var fileStream = Files.lines(toPath(path))) {
            cryptoCurrencyList = fileStream
                    .skip(1)
                    .map(this::toCryptoCurrency)
                    .toList();

        } catch (IOException e) {
            log.error("There is an exception during reading csv file from path = {}. Exception message: {}", path, e.getMessage());
            throw new DataReaderException("There is an exception during reading csv file from path = %s.".formatted(path), e);
        }
        return cryptoCurrencyList;
    }

    private Path toPath(String path) throws IOException {
        // try to find file in the system
        var pathToFile = Path.of(path);
        if (Files.exists(pathToFile)) {
            return pathToFile;
        }

        // try to find file in the classpath
        var file = new ClassPathResource(path).getFile();
        return file.toPath();
    }

    private CryptoCurrencyDataReaderResponse toCryptoCurrency(String line) {
        var attributes = line.split(COMMA_DELIMITER);
        if (attributes.length != ATTRIBUTES_LENGTH) {
            log.error("There is an exception during reading file line = {}. Wrong number of attributes (Have = {} , Need = {})", line, attributes.length, ATTRIBUTES_LENGTH);
            throw new DataReaderException("There is an exception during reading file line = %s. Wrong number of attributes (Have = %d , Need = %d)".formatted(line, attributes.length, ATTRIBUTES_LENGTH));
        }

        return new CryptoCurrencyDataReaderResponse(
                new Timestamp(Long.parseLong(attributes[0])),
                attributes[1],
                new BigDecimal(attributes[2]));
    }
}
