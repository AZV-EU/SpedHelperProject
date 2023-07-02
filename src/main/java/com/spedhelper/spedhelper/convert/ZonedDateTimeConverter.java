package com.spedhelper.spedhelper.convert;

import java.time.ZonedDateTime;

import org.springframework.core.convert.converter.Converter;

public class ZonedDateTimeConverter implements Converter<String, ZonedDateTime> {
    @Override
    public ZonedDateTime convert(String source) {
        return ZonedDateTime.parse(source);
    }
}
