package com.spedhelper.spedhelper.security;

import org.springframework.security.core.AuthenticationException;

public class ApiKeyBillingMaxedOutException extends AuthenticationException {
    public ApiKeyBillingMaxedOutException(String msg) {
        super(msg);
    }
}
