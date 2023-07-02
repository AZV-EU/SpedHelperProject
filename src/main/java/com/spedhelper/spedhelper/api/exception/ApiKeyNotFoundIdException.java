package com.spedhelper.spedhelper.api.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

import lombok.AllArgsConstructor;

@AllArgsConstructor
@ResponseStatus(HttpStatus.NOT_FOUND)
public class ApiKeyNotFoundIdException extends RuntimeException {
    private final Long apiKeyId;

    @Override
    public String getMessage() {
        return "ApiKey with id = " + apiKeyId + " not found.";
    }
}
