package com.spedhelper.spedhelper.api.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

import lombok.AllArgsConstructor;

@AllArgsConstructor
@ResponseStatus(HttpStatus.NOT_FOUND)
public class ApiKeyCreationLimitReached extends RuntimeException {
    @Override
    public String getMessage() {
        return "ApiKey creation limit reached.";
    }
}
