package com.spedhelper.spedhelper.api.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

import lombok.AllArgsConstructor;

@AllArgsConstructor
@ResponseStatus(HttpStatus.NOT_FOUND)
public class CustomerNotFoundException extends RuntimeException {
    private final Long customerId;

    @Override
    public String getMessage() {
        return "Customer with id = " + customerId + " not found.";
    }
}
