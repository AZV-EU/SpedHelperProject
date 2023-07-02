package com.spedhelper.spedhelper.security;

import java.io.IOException;
import java.time.ZonedDateTime;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.security.web.util.matcher.RequestMatcher;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.spedhelper.spedhelper.database.ApiKeyRepository;
import com.spedhelper.spedhelper.entities.ApiKey;
import com.spedhelper.spedhelper.entities.ApiKeyBillingType;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class ApiKeyAuthenticationFilter extends OncePerRequestFilter {
    private static final RequestMatcher matcher = new AntPathRequestMatcher("/api/**");

    @Autowired ApiKeyRepository apiRepo;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        if (matcher.matches(request))
        {
            Optional<ApiKey> apiKeyQuery = apiRepo
                .findByApiKey((request).getHeader("X-API-KEY"));
            if (apiKeyQuery.isPresent())
            {
                ApiKey apiKey = apiKeyQuery.get();
                ApiKeyAuthenticationToken auth = new ApiKeyAuthenticationToken(apiKey, apiKey.getOwner().getAuthorities());
                if (apiKey.getOwner().isEnabled() && (apiKey.getBillingType() == ApiKeyBillingType.DEBUG || apiKey.getRequestsSinceLastBillingCount() < apiKey.getBillingType().getMaxRequests())) {
                    apiKey.setTotalRequestsCount(apiKey.getTotalRequestsCount()+1);
                    apiKey.setLastRequestDate(ZonedDateTime.now());
                    apiKey.setRequestsSinceLastBillingCount(apiKey.getRequestsSinceLastBillingCount()+1);
                    apiRepo.save(apiKey);
                    SecurityContextHolder.getContext().setAuthentication(auth);
                } else
                    throw new ApiKeyBillingMaxedOutException("Api Key Requests Limit Reached");
            }
        }
        filterChain.doFilter(request, response);
    }
    
}
