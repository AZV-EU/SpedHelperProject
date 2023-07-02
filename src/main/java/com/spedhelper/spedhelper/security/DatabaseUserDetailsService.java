package com.spedhelper.spedhelper.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.spedhelper.spedhelper.database.CustomerRepository;
import com.spedhelper.spedhelper.entities.Customer;

@Service("databaseUserDetailsService")
public class DatabaseUserDetailsService implements UserDetailsService {
    @Autowired CustomerRepository cRepo;

    @Transactional(readOnly=true)
    @Override
    public Customer loadUserByUsername(String username) throws UsernameNotFoundException {
        Customer customer = cRepo.findByEmail(username).orElseThrow(() -> new UsernameNotFoundException(username));
        return customer;
    }
}
