package com.spedhelper.spedhelper.convert;

import java.util.Arrays;
import java.util.List;

import jakarta.persistence.AttributeConverter;

// https://stackoverflow.com/questions/287201/how-to-persist-a-property-of-type-liststring-in-jpa
public class StringListConverter implements AttributeConverter<List<String>, String> {
    private static final String SPLIT_CHAR = ";";

    @Override
    public String convertToDatabaseColumn(List<String> strList) {
        return strList != null ? String.join(SPLIT_CHAR, strList) : "";
    }

    @Override
    public List<String> convertToEntityAttribute(String str) {
        return (str != null && !str.isEmpty()) ? Arrays.asList(str.split(SPLIT_CHAR)) : Arrays.asList();
    }
}
