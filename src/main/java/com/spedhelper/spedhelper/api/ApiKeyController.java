package com.spedhelper.spedhelper.api;

import java.util.List;
import java.util.Optional;

import org.apache.commons.lang3.RandomStringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.spedhelper.spedhelper.api.exception.ApiKeyCreationLimitReached;
import com.spedhelper.spedhelper.api.exception.ApiKeyNotFoundIdException;
import com.spedhelper.spedhelper.api.exception.ApiKeyNotFoundKeyException;
import com.spedhelper.spedhelper.api.exception.CustomerNotFoundException;
import com.spedhelper.spedhelper.database.ApiKeyRepository;
import com.spedhelper.spedhelper.database.CustomerRepository;
import com.spedhelper.spedhelper.entities.ApiKey;
import com.spedhelper.spedhelper.entities.Customer;

@RestController
@RequestMapping("/api")
public class ApiKeyController {
    Logger logger = LoggerFactory.getLogger(TripController.class);
    @Autowired ApiKeyRepository apiRepo;
    @Autowired CustomerRepository cRepo;

    @GetMapping("/apikeys")
    public ResponseEntity<List<ApiKey>> getAllApiKeys(
        @AuthenticationPrincipal ApiKey principal
    ) {
        Customer customer = principal.getOwner();
        return ResponseEntity.ok().body(apiRepo.findAllByOwner(customer));
    }

    @GetMapping("/admin/apikeys")
    public ResponseEntity<List<ApiKey>> adminGetAllApiKeys() {
        return ResponseEntity.ok().body(apiRepo.findAll());
    }

    @GetMapping("/apikey/{id}")
    public ResponseEntity<ApiKey> getById(
        @AuthenticationPrincipal ApiKey principal,
        @PathVariable Long id
    ) {
        Customer customer = principal.getOwner();
        Optional<ApiKey> apiKey = apiRepo.findByOwnerAndId(customer, id);
        if (apiKey.isPresent())
            return ResponseEntity.ok().body(apiKey.get());
        throw new ApiKeyNotFoundIdException(id);
    }

    @GetMapping("/admin/apikey/{id}")
    public ResponseEntity<ApiKey> adminGetById(
        @PathVariable Long id
    ) {
        Optional<ApiKey> apiKey = apiRepo.findById(id);
        if (apiKey.isPresent())
            return ResponseEntity.ok().body(apiKey.get());
        throw new ApiKeyNotFoundIdException(id);
    }

    @GetMapping("/admin/apikey/customer/{id}")
    public ResponseEntity<List<ApiKey>> adminGetByCustomerId(
        @PathVariable Long id
    ) {
        Optional<Customer> customer = cRepo.findById(id);
        if (customer.isPresent())
            return ResponseEntity.ok().body(customer.get().getApiKeys());
        throw new CustomerNotFoundException(id);
    }

    @GetMapping("/apikey/key/{apikey}")
    public ResponseEntity<ApiKey> getByApiKey(
        @AuthenticationPrincipal ApiKey principal,
        @PathVariable String apikey) {
        Customer customer = principal.getOwner();
        Optional<ApiKey> apiKey = apiRepo.findByOwnerAndApiKey(customer, apikey);
        if (apiKey.isPresent())
            return ResponseEntity.ok().body(apiKey.get());
        throw new ApiKeyNotFoundKeyException(apikey);
    }

    @GetMapping("/admin/apikey/key/{apikey}")
    public ResponseEntity<ApiKey> adminGetByApiKey(
        @PathVariable String apikey) {
        Optional<ApiKey> apiKey = apiRepo.findByApiKey(apikey);
        if (apiKey.isPresent())
            return ResponseEntity.ok().body(apiKey.get());
        throw new ApiKeyNotFoundKeyException(apikey);
    }

    private static final String API_KEY_CHARACTERS = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789~`!@#$%^&*()-_=+[{]}\\|;:,<.>/?";
    private ApiKey GenerateApiKey() {
        ApiKey apiKey = new ApiKey();
        apiKey.setApiKey(RandomStringUtils.random(64, API_KEY_CHARACTERS));
        return apiKey;
    }

    @PostMapping("/apikey")
    public ResponseEntity<ApiKey> createApiKey(
        @AuthenticationPrincipal ApiKey principal
    ) {
        Customer customer = principal.getOwner();
        if (apiRepo.countByOwner(customer) < 10)
        {
            ApiKey apiKey = GenerateApiKey();
            apiKey.setOwner(customer);
            return ResponseEntity.ok().body(apiRepo.save(apiKey));
        }
        throw new ApiKeyCreationLimitReached();
    }
    
    @PostMapping("/admin/apikey/{customerId}")
    public ResponseEntity<ApiKey> adminCreateApiKey(
        @PathVariable Long customerId
    ) {
        Optional<Customer> customer = cRepo.findById(customerId);
        if (customer.isPresent())
        {
            ApiKey apiKey = GenerateApiKey();
            apiKey.setOwner(customer.get());
            return ResponseEntity.ok().body(apiRepo.save(apiKey));
        }
        throw new CustomerNotFoundException(customerId);
    }

    @PutMapping("/admin/apikey")
    public ResponseEntity<ApiKey> adminUpdateApiKey(
        @RequestBody ApiKey apiKey
    ) {
        Optional<ApiKey> oldApiKey = apiRepo.findById(apiKey.getId());
        if (oldApiKey.isPresent())
        {
            apiKey.setOwner(oldApiKey.get().getOwner());
            apiKey.setApiKey(apiKey.getApiKey() == null ? oldApiKey.get().getApiKey() : apiKey.getApiKey());
            return ResponseEntity.ok().body(apiRepo.save(apiKey));
        }
        throw new ApiKeyNotFoundIdException(apiKey.getId());
    }

    @DeleteMapping("/apikey/{id}")
    public ResponseEntity<HttpStatus> deleteApiKey(
        @AuthenticationPrincipal ApiKey principal,
        @PathVariable Long id
    ) {
        Customer customer = principal.getOwner();
        Optional<ApiKey> oldApiKey = apiRepo.findByOwnerAndId(customer, id);
        if (oldApiKey.isPresent())
        {
            try {
                apiRepo.deleteById(id);
                return ResponseEntity.ok().build();
            } catch (Exception e) {
                logger.error("deleteApiKey", e);
                return ResponseEntity.internalServerError().build();
            }
        }
        throw new ApiKeyNotFoundIdException(id);
    }

    @DeleteMapping("/admin/apikey/{id}")
    public ResponseEntity<HttpStatus> adminDeleteApiKey(
        @PathVariable Long id
    ) {
        Optional<ApiKey> oldApiKey = apiRepo.findById(id);
        if (oldApiKey.isPresent())
        {
            try {
                apiRepo.deleteById(id);
                return ResponseEntity.ok().build();
            } catch (Exception e) {
                logger.error("adminDeleteApiKey", e);
                return ResponseEntity.internalServerError().build();
            }
        }
        throw new ApiKeyNotFoundIdException(id);
    }
}
