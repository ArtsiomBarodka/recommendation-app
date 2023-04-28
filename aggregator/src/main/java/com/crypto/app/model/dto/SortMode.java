package com.crypto.app.model.dto;

public enum SortMode {
    ASC,
    DESC;


    public static SortMode of(String sortMode) {
        return DESC.name().equalsIgnoreCase(sortMode) ? DESC : ASC;
    }
}
