package com.spedhelper.spedhelper.api.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

import lombok.AllArgsConstructor;

@AllArgsConstructor
@ResponseStatus(HttpStatus.NOT_FOUND)
public class TripNotFoundException extends RuntimeException {
    private final Long tripId;

    @Override
    public String getMessage() {
        return "Trip with id = " + tripId + " not found.";
    }
}
