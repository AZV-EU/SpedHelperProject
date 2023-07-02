package com.spedhelper.spedhelper.database;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.spedhelper.spedhelper.entities.Customer;
import com.spedhelper.spedhelper.entities.Vehicle;

public interface VehicleRepository extends JpaRepository<Vehicle, Long> {
    List<Vehicle> findAllByOwner(Customer owner);
    Optional<Vehicle> findByOwnerAndId(Customer owner, Long id);
    void deleteByOwnerAndId(Customer owner, Long id);
}
