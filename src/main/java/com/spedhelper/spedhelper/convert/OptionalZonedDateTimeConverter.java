package com.spedhelper.spedhelper.convert;

import java.time.ZonedDateTime;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.convert.converter.Converter;

public class OptionalZonedDateTimeConverter implements Converter<String, Optional<ZonedDateTime>> {
    private static final Logger logger = LoggerFactory.getLogger(OptionalZonedDateTimeConverter.class);
    @Override
    public Optional<ZonedDateTime> convert(String source) {
        if (source == null)
        {
            logger.warn("Source String was null");
            return Optional.empty();
        }
        try {
            return Optional.of(ZonedDateTime.parse(source));
        } catch (Exception e) {
            logger.warn("Could not parse '" + source + "' to ZonedDateTime");
            return Optional.empty();
        }
    }
}
