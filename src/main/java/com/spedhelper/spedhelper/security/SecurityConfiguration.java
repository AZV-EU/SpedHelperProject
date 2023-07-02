package com.spedhelper.spedhelper.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class SecurityConfiguration {
    @Autowired
    ApplicationContext context;

    @Autowired
    @Qualifier("databaseUserDetailsService")
    DatabaseUserDetailsService databaseUserDetailsService;
    
    @Autowired
    @Qualifier("databaseUserDetailsPasswordService")
    DatabaseUserDetailsPasswordService databaseUserDetailsPasswordService;

    @Bean
    PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    AuthenticationManager authenticationManager(HttpSecurity http) throws Exception {
        AuthenticationManagerBuilder auth =
            http.getSharedObject(AuthenticationManagerBuilder.class);
        auth.userDetailsService(databaseUserDetailsService)
            .userDetailsPasswordManager(databaseUserDetailsPasswordService)
            .passwordEncoder(passwordEncoder());
        return auth.build();
    }

    @Bean
    SecurityFilterChain apiKeyFilterChain(HttpSecurity http) throws Exception {
        http.csrf(csrf -> csrf.disable())
            .addFilterBefore(context.getBean(ApiKeyAuthenticationFilter.class), BasicAuthenticationFilter.class)
            .authorizeHttpRequests(auth ->
                auth.requestMatchers("/api/**")
                        .authenticated()
                    .requestMatchers("/api/admin/**")
                        .hasAnyRole("DEBUG", "ADMIN")
                    .requestMatchers("/", "/login", "/wip", "/error")
                        .permitAll()
                    .anyRequest()
                        .authenticated()
            );
        return http.build();
    }
}