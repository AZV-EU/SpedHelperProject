package com.spedhelper.spedhelper.api;

import java.time.ZonedDateTime;
import java.util.List;
import java.util.Optional;

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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.spedhelper.spedhelper.api.exception.CustomerNotFoundException;
import com.spedhelper.spedhelper.api.exception.TripNotFoundException;
import com.spedhelper.spedhelper.api.exception.VehicleNotFoundException;
import com.spedhelper.spedhelper.database.CustomerRepository;
import com.spedhelper.spedhelper.database.TripRepository;
import com.spedhelper.spedhelper.database.VehicleRepository;
import com.spedhelper.spedhelper.entities.ApiKey;
import com.spedhelper.spedhelper.entities.Customer;
import com.spedhelper.spedhelper.entities.Trip;
import com.spedhelper.spedhelper.entities.Vehicle;

import jakarta.transaction.Transactional;

@RestController
@RequestMapping("/api")
public class TripController {
    Logger logger = LoggerFactory.getLogger(TripController.class);
    @Autowired TripRepository tripRepo;
    @Autowired VehicleRepository vehRepo;
    @Autowired CustomerRepository cRepo;

    @GetMapping("/trips")
    public ResponseEntity<List<Trip>> getAllTrips(
        @AuthenticationPrincipal ApiKey principal
    ) {
        Customer customer = principal.getOwner();
        return ResponseEntity.ok().body(tripRepo.findAllByVehicleOwner(customer));
    }

    @GetMapping("/admin/trips")
    public ResponseEntity<List<Trip>> adminGetAllTrips() {
        return ResponseEntity.ok().body(tripRepo.findAll());
    }

    @GetMapping("/trip/{id}")
    public ResponseEntity<Trip> getTripById(
        @AuthenticationPrincipal ApiKey principal,
        @PathVariable Long id
    ) {
        Customer customer = principal.getOwner();
        Optional<Trip> trip = tripRepo.findByIdAndVehicleOwner(id, customer);
        if (trip.isPresent())
            return ResponseEntity.ok().body(trip.get());
        throw new TripNotFoundException(id);
    }

    @GetMapping("/admin/trip/{id}")
    public ResponseEntity<Trip> adminGetTripById(
        @PathVariable Long id
    ) {
        Optional<Trip> trip = tripRepo.findById(id);
        if (trip.isPresent())
            return ResponseEntity.ok().body(trip.get());
        throw new TripNotFoundException(id);
    }

    @GetMapping("/trip/vehicle/{vehicleId}")
    public ResponseEntity<List<Trip>> getByVehicleId(
        @AuthenticationPrincipal ApiKey principal,
        @PathVariable Long vehicleId,
        @RequestParam Optional<ZonedDateTime> startDate,
        @RequestParam Optional<ZonedDateTime> endDate
    ) {
        Customer customer = principal.getOwner();
        Optional<Vehicle> vehicle = vehRepo.findByOwnerAndId(customer, vehicleId);
        if (vehicle.isPresent())
        {
            if (startDate.isPresent() && endDate.isPresent())
                return ResponseEntity.ok().body(
                    tripRepo.findAllByVehicleAndDateBetween(
                        vehicle.get(), startDate.get(), endDate.get()
                    )
                );
            else if (startDate.isPresent())
                return ResponseEntity.ok().body(
                    tripRepo.findAllByVehicleAndDateGreaterThanEqual(
                        vehicle.get(), startDate.get()
                    )
                );
            else if (endDate.isPresent())
                return ResponseEntity.ok().body(
                    tripRepo.findAllByVehicleAndDateLessThanEqual(
                        vehicle.get(), endDate.get()
                    )
                );
            return ResponseEntity.ok().body(tripRepo.findAllByVehicle(vehicle.get()));
        }
        throw new VehicleNotFoundException(vehicleId);
    }

    @GetMapping("/admin/trip/vehicle/{vehicleId}")
    public ResponseEntity<List<Trip>> adminGetByVehicleId(
        @PathVariable Long vehicleId,
        @RequestParam Optional<ZonedDateTime> startDate,
        @RequestParam Optional<ZonedDateTime> endDate
    ) {
        Optional<Vehicle> vehicle = vehRepo.findById(vehicleId);
        if (vehicle.isPresent())
        {
            if (startDate.isPresent() && endDate.isPresent())
                return ResponseEntity.ok().body(
                    tripRepo.findAllByVehicleAndDateBetween(
                        vehicle.get(), startDate.get(), endDate.get()
                    )
                );
            else if (startDate.isPresent())
                return ResponseEntity.ok().body(
                    tripRepo.findAllByVehicleAndDateGreaterThanEqual(
                        vehicle.get(), startDate.get()
                    )
                );
            else if (endDate.isPresent())
                return ResponseEntity.ok().body(
                    tripRepo.findAllByVehicleAndDateLessThanEqual(
                        vehicle.get(), endDate.get()
                    )
                );
            return ResponseEntity.ok().body(tripRepo.findAllByVehicle(vehicle.get()));
        }
        throw new VehicleNotFoundException(vehicleId);
    }

    @PostMapping("/trip")
    public ResponseEntity<Trip> createTrip(
        @AuthenticationPrincipal ApiKey principal,
        @RequestBody Trip trip
    ) {
        Customer customer = principal.getOwner();
        Optional<Vehicle> vehicle = vehRepo.findByOwnerAndId(customer, trip.getVehicle().getId());
        if (vehicle.isPresent())
        {
            try {
                trip.setId(null);
                trip.setVehicle(vehicle.get());
                return ResponseEntity.ok().body(tripRepo.save(trip));
            } catch (Exception e) {
                logger.error("createTrip", e);
                return ResponseEntity.internalServerError().build();
            }
        }
        throw new VehicleNotFoundException(trip.getVehicle().getId());
    }

    @PostMapping("/admin/trip/{customerId}")
    public ResponseEntity<Trip> adminCreateTrip(
        @PathVariable Long customerId,
        @RequestBody Trip trip
    ) {
        Optional<Customer> customer = cRepo.findById(customerId);
        if (customer.isPresent())
        {
            Optional<Vehicle> vehicle = vehRepo.findByOwnerAndId(customer.get(), trip.getVehicle().getId());
            if (vehicle.isPresent())
            {
                try {
                    trip.setId(null);
                    trip.setVehicle(vehicle.get());
                    return ResponseEntity.ok().body(tripRepo.save(trip));
                } catch (Exception e) {
                    logger.error("createTrip", e);
                    return ResponseEntity.internalServerError().build();
                }
            }
            throw new VehicleNotFoundException(trip.getVehicle().getId());
        }
        throw new CustomerNotFoundException(customerId);
    }

    @PutMapping("/trip")
    public ResponseEntity<Trip> updateTrip(
        @AuthenticationPrincipal ApiKey principal,
        @RequestBody Trip trip) {
        Customer customer = principal.getOwner();
        Optional<Trip> oldTrip = tripRepo.findByVehicleOwnerAndId(customer, trip.getId());
        if (oldTrip.isPresent())
        {
            try {
                return ResponseEntity.ok().body(tripRepo.save(trip));
            } catch (Exception e) {
                logger.error("updateTrip", e);
                return ResponseEntity.internalServerError().build();
            }
        }
        throw new TripNotFoundException(trip.getId());
    }

    @PutMapping("/admin/trip")
    public ResponseEntity<Trip> adminUpdateTrip(
        @RequestBody Trip trip) {
        Optional<Trip> oldTrip = tripRepo.findById(trip.getId());
        if (oldTrip.isPresent())
        {
            try {
                return ResponseEntity.ok().body(tripRepo.save(trip));
            } catch (Exception e) {
                logger.error("updateTrip", e);
                return ResponseEntity.internalServerError().build();
            }
        }
        throw new TripNotFoundException(trip.getId());
    }

    @Transactional
    @DeleteMapping("/trip/{id}")
    public ResponseEntity<HttpStatus> deleteTrip(
        @AuthenticationPrincipal ApiKey principal,
        @PathVariable Long id
    ) {
        Customer customer = principal.getOwner();
        Optional<Trip> oldTrip = tripRepo.findByVehicleOwnerAndId(customer, id);
        if (oldTrip.isPresent())
        {
            try {
                tripRepo.deleteById(oldTrip.get().getId());
                return ResponseEntity.ok().build();
            } catch (Exception e) {
                logger.error("deleteTrip", e);
                return ResponseEntity.internalServerError().build();
            }
        }
        throw new TripNotFoundException(id);
    }

    @Transactional
    @DeleteMapping("/admin/trip/{id}")
    public ResponseEntity<HttpStatus> adminDeleteTrip(
        @PathVariable Long id
    ) {
        Optional<Trip> oldTrip = tripRepo.findById(id);
        if (oldTrip.isPresent())
        {
            try {
                tripRepo.deleteById(oldTrip.get().getId());
                return ResponseEntity.ok().build();
            } catch (Exception e) {
                logger.error("deleteTrip", e);
                return ResponseEntity.internalServerError().build();
            }
        }
        throw new TripNotFoundException(id);
    }
}
