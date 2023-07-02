package com.spedhelper.spedhelper.database;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.spedhelper.spedhelper.entities.ApiKey;
import com.spedhelper.spedhelper.entities.Customer;

public interface ApiKeyRepository extends JpaRepository<ApiKey, Long> {
    Optional<ApiKey> findByOwnerAndId(Customer customer, Long id);
    
    Long countByOwner(Customer customer);

    Optional<ApiKey> findByApiKey(String apiKey);
    Optional<ApiKey> findByOwnerAndApiKey(Customer customer, String apikey);

    List<ApiKey> findAllByOwner(Customer customer);

}
