package com.spedhelper.spedhelper.security;

import java.util.Collection;

import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;

import com.spedhelper.spedhelper.entities.ApiKey;

public class ApiKeyAuthenticationToken extends AbstractAuthenticationToken {
    private final ApiKey apiKey;

    public ApiKeyAuthenticationToken(ApiKey apiKey, Collection<? extends GrantedAuthority> authorities) {
        super(authorities);
        this.apiKey = apiKey;
        setAuthenticated(true);
    }

    @Override
    public Object getCredentials() {
        return null;
    }

    @Override
    public ApiKey getPrincipal() {
        return apiKey;
    }

    @Override
    public Object getDetails() {
        return apiKey.getOwner();
    }
}
