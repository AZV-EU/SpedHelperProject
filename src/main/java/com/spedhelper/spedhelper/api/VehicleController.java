package com.spedhelper.spedhelper.api;
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
import org.springframework.web.bind.annotation.RestController;

import com.spedhelper.spedhelper.api.exception.CustomerNotFoundException;
import com.spedhelper.spedhelper.api.exception.VehicleNotFoundException;
import com.spedhelper.spedhelper.database.CustomerRepository;
import com.spedhelper.spedhelper.database.VehicleRepository;
import com.spedhelper.spedhelper.entities.ApiKey;
import com.spedhelper.spedhelper.entities.Customer;
import com.spedhelper.spedhelper.entities.Vehicle;

import jakarta.transaction.Transactional;

@RestController
@RequestMapping("/api")
class VehicleController {
    @Autowired VehicleRepository vehRepo;
    @Autowired CustomerRepository cRepo;
    Logger logger = LoggerFactory.getLogger(VehicleController.class);

    @GetMapping("/vehicles")
    public ResponseEntity<List<Vehicle>> getAllVehicles(
        @AuthenticationPrincipal ApiKey principal
    ) {
        Customer customer = principal.getOwner();
        return ResponseEntity.ok().body(vehRepo.findAllByOwner(customer));
    }

    @GetMapping("/admin/vehicles")
    public ResponseEntity<List<Vehicle>> adminGetAllVehicles() {
        return ResponseEntity.ok().body(vehRepo.findAll());
    }

    @GetMapping("/vehicle/{id}")
    public ResponseEntity<Vehicle> getVehicle(
        @AuthenticationPrincipal ApiKey principal,
        @PathVariable Long id
    ) {
        Customer customer = principal.getOwner();
        Optional<Vehicle> vehicle = vehRepo.findByOwnerAndId(customer, id);
        if (vehicle.isPresent())
            return ResponseEntity.ok().body(vehicle.get());
        throw new VehicleNotFoundException(id);
    }

    @GetMapping("/admin/vehicle/{id}")
    public ResponseEntity<Vehicle> adminGetVehicle(
        @PathVariable Long id
    ) {
        Optional<Vehicle> vehicle = vehRepo.findById(id);
        if (vehicle.isPresent())
            return ResponseEntity.ok().body(vehicle.get());
        throw new VehicleNotFoundException(id);
    }

    @PostMapping("/vehicle")
    public ResponseEntity<Vehicle> createVehicle(
        @AuthenticationPrincipal ApiKey principal,
        @RequestBody Vehicle vehicle
    ) {
        Customer customer = principal.getOwner();
        try {
            vehicle.setId(null);
            vehicle.setOwner(customer);
            return ResponseEntity.ok().body(vehRepo.save(vehicle));
        } catch (Exception e) {
            logger.error("createVehicle", e);
            return ResponseEntity.internalServerError().build();
        }
    }

    @PostMapping("/admin/vehicle/{customerId}")
    public ResponseEntity<Vehicle> adminCreateVehicle(
        @PathVariable Long customerId,
        @RequestBody Vehicle vehicle
    ) {
        Optional<Customer> customer = cRepo.findById(customerId);
        if (customer.isPresent())
            try {
                vehicle.setId(null);
                vehicle.setOwner(customer.get());
                return ResponseEntity.ok().body(vehRepo.save(vehicle));
            } catch (Exception e) {
                logger.error("createVehicle", e);
                return ResponseEntity.internalServerError().build();
            }
        throw new CustomerNotFoundException(customerId);
    }

    @PutMapping("/vehicle")
    public ResponseEntity<Vehicle> updateVehicle(
        @AuthenticationPrincipal ApiKey principal,
        @RequestBody Vehicle vehicle
    ) {
        Customer customer = principal.getOwner();
        Optional<Vehicle> oldVehicle = vehRepo.findByOwnerAndId(customer, vehicle.getId());
        if (oldVehicle.isPresent()) {
            vehicle.setOwner(customer);
            vehicle.setTrips(oldVehicle.get().getTrips());
            return ResponseEntity.ok().body(vehRepo.save(vehicle));
        }
        throw new VehicleNotFoundException(vehicle.getId());
    }

    @PutMapping("/admin/vehicle")
    public ResponseEntity<Vehicle> adminUpdateVehicle(
        @RequestBody Vehicle vehicle
    ) {
        Optional<Vehicle> oldVehicle = vehRepo.findById(vehicle.getId());
        if (oldVehicle.isPresent()) {
            vehicle.setOwner(oldVehicle.get().getOwner());
            vehicle.setTrips(oldVehicle.get().getTrips());
            return ResponseEntity.ok().body(vehRepo.save(vehicle));
        }
        throw new VehicleNotFoundException(vehicle.getId());
    }

    @Transactional
    @DeleteMapping("/vehicle/{id}")
    public ResponseEntity<HttpStatus> deleteVehicle(
        @AuthenticationPrincipal ApiKey principal,
        @PathVariable Long id
    ) {
        Customer customer = principal.getOwner();
        Optional<Vehicle> vehicle = vehRepo.findByOwnerAndId(customer, id);
        if (vehicle.isPresent())
        {
            try {
                vehRepo.deleteByOwnerAndId(customer, id);
                return ResponseEntity.ok().build();
            } catch (Exception e) {
                logger.error("deleteVehicle", e);
                return ResponseEntity.internalServerError().build();
            }
        }
        throw new VehicleNotFoundException(id);
    }

    @Transactional
    @DeleteMapping("/admin/vehicle/{id}")
    public ResponseEntity<HttpStatus> adminDeleteVehicle(
        @PathVariable Long id
    ) {
        Optional<Vehicle> vehicle = vehRepo.findById(id);
        if (vehicle.isPresent())
        {
            try {
                vehRepo.deleteById(id);
                return ResponseEntity.ok().build();
            } catch (Exception e) {
                logger.error("deleteVehicle", e);
                return ResponseEntity.internalServerError().build();
            }
        }
        throw new VehicleNotFoundException(id);
    }
}
