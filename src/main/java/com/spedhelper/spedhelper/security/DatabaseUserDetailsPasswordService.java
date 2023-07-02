package com.spedhelper.spedhelper.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsPasswordService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.spedhelper.spedhelper.database.CustomerRepository;
import com.spedhelper.spedhelper.entities.Customer;

@Service("databaseUserDetailsPasswordService")
public class DatabaseUserDetailsPasswordService implements UserDetailsPasswordService {
    @Autowired CustomerRepository cRepo;

    @Override
    public UserDetails updatePassword(UserDetails user, String newPassword) {
        Customer customer = cRepo.findByEmail(user.getUsername()).orElseThrow(() -> new UsernameNotFoundException(user.getUsername()));
        customer.setPasswordHash(newPassword);
        cRepo.saveAndFlush(customer);
        return user;
    }

}
