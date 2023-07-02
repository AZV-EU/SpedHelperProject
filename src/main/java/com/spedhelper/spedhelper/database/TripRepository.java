package com.spedhelper.spedhelper.database;

import java.time.ZonedDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.spedhelper.spedhelper.entities.Customer;
import com.spedhelper.spedhelper.entities.Trip;
import com.spedhelper.spedhelper.entities.Vehicle;

public interface TripRepository extends JpaRepository<Trip, Long>{
    List<Trip> findAllByVehicle(Vehicle vehicle);
    List<Trip> findAllByVehicleOwner(Customer customer);

    List<Trip> findAllByVehicleOwnerAndDateAfter(Customer owner, ZonedDateTime date);
    List<Trip> findAllByVehicleOwnerAndDateBefore(Customer owner, ZonedDateTime date);
    List<Trip> findAllByVehicleOwnerAndDateBetween(Customer owner, ZonedDateTime startDate, ZonedDateTime endDate);

    List<Trip> findAllByVehicleAndDateLessThanEqual(Vehicle vehicle, ZonedDateTime date);
    List<Trip> findAllByVehicleAndDateGreaterThanEqual(Vehicle vehicle, ZonedDateTime date);
    List<Trip> findAllByVehicleAndDateBetween(Vehicle vehicle, ZonedDateTime startDate, ZonedDateTime endDate);

    List<Trip> findAllByVehicleOwnerAndVehicleModel(Customer owner, String model);
    List<Trip> findAllByVehicleOwnerAndVehicleModelAndDateLessThanEqual(Customer owner, String model, ZonedDateTime date);
    List<Trip> findAllByVehicleOwnerAndVehicleModelAndDateGreaterThanEqual(Customer owner, String model, ZonedDateTime date);
    List<Trip> findAllByVehicleOwnerAndVehicleModelAndDateBetween(Customer owner, String model, ZonedDateTime startDate, ZonedDateTime endDate);

    Optional<Trip> findByIdAndVehicleOwner(Long id, Customer customer);
    Optional<Trip> findByVehicleOwnerAndId(Customer customer, Long id);
    List<Trip> findAllByVehicleModel(String string);
}
