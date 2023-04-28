package com.crypto.app.exception;

public class DataReaderException extends RuntimeException {
    public DataReaderException(String message) {
        super(message);
    }

    public DataReaderException(String message, Throwable cause) {
        super(message, cause);
    }
}
