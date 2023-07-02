package com.spedhelper.spedhelper.api;

import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.spedhelper.spedhelper.api.exception.CustomerNotFoundException;
import com.spedhelper.spedhelper.database.CustomerRepository;
import com.spedhelper.spedhelper.entities.Customer;

@RestController
@RequestMapping("/api/admin")
public class CustomerController {
    Logger logger = LoggerFactory.getLogger(TripController.class);
    @Autowired CustomerRepository cRepo;

    @GetMapping("/customers")
    public ResponseEntity<List<Customer>> adminGetAllCustomers() {
        return ResponseEntity.ok().body(cRepo.findAll());
    }

    @GetMapping("/customer/{id}")
    public ResponseEntity<Customer> adminGetCustomerById(
        @PathVariable Long id
    ) {
        Optional<Customer> customer = cRepo.findById(id);
        if (customer.isPresent())
            return ResponseEntity.ok().body(customer.get());
        throw new CustomerNotFoundException(id);
    }

    @PostMapping("/customer")
    public ResponseEntity<Customer> adminCreateCustomer(
        @RequestBody Customer newCustomer
    ) {
        newCustomer.setId(null);
        return ResponseEntity.ok().body(cRepo.save(newCustomer));
    }

    @PutMapping("/customer")
    public ResponseEntity<Customer> adminUpdateCustomer(
        @RequestBody Customer updatedCustomer
    ) {
        Optional<Customer> oldCustomer = cRepo.findById(updatedCustomer.getId());
        if (oldCustomer.isPresent())
            return ResponseEntity.ok().body(oldCustomer.get());
        throw new CustomerNotFoundException(updatedCustomer.getId());
    }

    @DeleteMapping("/customer/{id}")
    public ResponseEntity<HttpStatus> adminDeleteById(
        @PathVariable Long id
    ) {
        Optional<Customer> oldCustomer = cRepo.findById(id);
        if (oldCustomer.isPresent())
            try {
                cRepo.deleteById(id);
                return ResponseEntity.ok().build();
            } catch (Exception e) {
                logger.error("adminDeleteById", e);
                return ResponseEntity.internalServerError().build();
            }
        throw new CustomerNotFoundException(id);
    }
}
