package com.spedhelper.spedhelper.api.advice;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import com.spedhelper.spedhelper.api.exception.ApiKeyCreationLimitReached;
import com.spedhelper.spedhelper.api.exception.ApiKeyNotFoundIdException;
import com.spedhelper.spedhelper.api.exception.ApiKeyNotFoundKeyException;
import com.spedhelper.spedhelper.api.exception.CustomerNotFoundException;
import com.spedhelper.spedhelper.api.exception.TripNotFoundException;
import com.spedhelper.spedhelper.api.exception.VehicleNotFoundException;

@ControllerAdvice
public class ApiExceptionHandler extends ResponseEntityExceptionHandler {
    @ExceptionHandler(
        {
            VehicleNotFoundException.class,
            TripNotFoundException.class,
            ApiKeyNotFoundIdException.class,
            ApiKeyNotFoundKeyException.class,
            ApiKeyCreationLimitReached.class,
            CustomerNotFoundException.class
        }
    )
    protected ResponseEntity<Object> handleNotFoundException(RuntimeException ex, WebRequest request) {
        return handleExceptionInternal(ex, ex.getMessage(), new HttpHeaders(), HttpStatus.NOT_FOUND, request);
    }
}
