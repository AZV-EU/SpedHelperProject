package com.spedhelper.spedhelper.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.format.FormatterRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import com.spedhelper.spedhelper.convert.OptionalZonedDateTimeConverter;
import com.spedhelper.spedhelper.convert.ZonedDateTimeConverter;

@Configuration
public class ZonedDateTimeFormatConfig implements WebMvcConfigurer {
    
    @Override
    public void addFormatters(FormatterRegistry registry) {
        registry.addConverter(new ZonedDateTimeConverter());
        registry.addConverter(new OptionalZonedDateTimeConverter());
    }
}
