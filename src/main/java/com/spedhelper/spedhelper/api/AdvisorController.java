package com.spedhelper.spedhelper.api;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.spedhelper.spedhelper.api.component.AdvisorReport;
import com.spedhelper.spedhelper.api.exception.VehicleNotFoundException;
import com.spedhelper.spedhelper.database.TripRepository;
import com.spedhelper.spedhelper.database.VehicleRepository;
import com.spedhelper.spedhelper.entities.ApiKey;
import com.spedhelper.spedhelper.entities.Customer;
import com.spedhelper.spedhelper.entities.Trip;
import com.spedhelper.spedhelper.entities.Vehicle;

@RestController
@RequestMapping("/api")
public class AdvisorController {
    @Autowired VehicleRepository vehRepo;
    @Autowired TripRepository tripRepo;
    
    @GetMapping("/advisor/report")
    public ResponseEntity<List<AdvisorReport>> getReport(
        @AuthenticationPrincipal ApiKey principal,
        @RequestParam Optional<Long> vehicle,
        @RequestParam Optional<String> model
    ) {
        Customer customer = principal.getOwner();
        if (vehicle.isPresent())
        {
            Optional<Vehicle> targetVehicle =
                vehRepo.findByOwnerAndId(customer, vehicle.get());
            if (targetVehicle.isPresent())
                return ResponseEntity.ok().body(
                    Arrays.asList(
                        new AdvisorReport(targetVehicle.get())
                    )
                );
            throw new VehicleNotFoundException(vehicle.get());
        } else if (model.isPresent()) {
            List<Trip> modelTrips = tripRepo.findAllByVehicleOwnerAndVehicleModel(
                customer,
                model.get()
            );
            HashSet<Vehicle> vehicles =
                modelTrips.stream()
                .map(Trip::getVehicle)
                .map(Vehicle::getId)
                .distinct()
                .map(vehRepo::findById)
                .filter(t -> t.isPresent())
                .map(Optional::get)
                .collect(Collectors.toCollection(HashSet::new));

            return ResponseEntity.ok().body(vehicles.stream().map(AdvisorReport::new).toList());
        }
        return ResponseEntity.ok().body(vehRepo.findAllByOwner(customer).stream().map(AdvisorReport::new).toList());
    }

    @GetMapping("/admin/advisor/report")
    public ResponseEntity<List<AdvisorReport>> adminGetReport(
        @RequestParam Optional<Long> vehicle,
        @RequestParam Optional<String> model
    ) {
        if (vehicle.isPresent())
        {
            Optional<Vehicle> targetVehicle =
                vehRepo.findById(vehicle.get());
            if (targetVehicle.isPresent())
                return ResponseEntity.ok().body(
                    Arrays.asList(
                        new AdvisorReport(targetVehicle.get())
                    )
                );
            throw new VehicleNotFoundException(vehicle.get());
        } else if (model.isPresent()) {
            List<Trip> modelTrips = tripRepo.findAllByVehicleModel(
                model.get()
            );
            HashSet<Vehicle> vehicles =
                modelTrips.stream()
                .map(Trip::getVehicle)
                .map(Vehicle::getId)
                .distinct()
                .map(vehRepo::findById)
                .filter(t -> t.isPresent())
                .map(Optional::get)
                .collect(Collectors.toCollection(HashSet::new));

            return ResponseEntity.ok().body(vehicles.stream().map(AdvisorReport::new).toList());
        }
        return ResponseEntity.ok().body(vehRepo.findAll().stream().map(AdvisorReport::new).toList());
    }
}
