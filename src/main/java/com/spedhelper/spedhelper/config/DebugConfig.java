package com.spedhelper.spedhelper.config;

import java.time.ZonedDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import com.spedhelper.spedhelper.database.CustomerRepository;
import com.spedhelper.spedhelper.entities.Customer;

@Configuration
public class DebugConfig {

    @Bean
    Customer debugCustomer(CustomerRepository cRepo) {
        Optional<Customer> debugCustomer = cRepo.findByEmail("debug@debugmail.com");
        Customer customer = debugCustomer.isPresent() ? debugCustomer.get() : new Customer();
        customer.setId(1l);
        customer.setEmail("debug@debugmail.com");
        customer.setForeName("Debug");
        customer.setLastName("Debug");
        customer.setPasswordHash(
            new BCryptPasswordEncoder().encode("debug")
        );
        customer.setAuthorities(
            List.of("ADMIN")
        );
        customer.setRegistrationDate(ZonedDateTime.now());
        cRepo.saveAndFlush(customer);
        return customer;
    }
}
