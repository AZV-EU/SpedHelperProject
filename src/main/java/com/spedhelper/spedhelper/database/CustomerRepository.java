package com.spedhelper.spedhelper.database;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.spedhelper.spedhelper.entities.Customer;

public interface CustomerRepository extends JpaRepository<Customer, Long> {
    public Optional<Customer> findByEmail(String email);
}
