package com.spedhelper.spedhelper.api.component;

import java.util.List;

import com.spedhelper.spedhelper.entities.Trip;
import com.spedhelper.spedhelper.entities.Vehicle;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class AdvisorReport {
    private Vehicle vehicle;
    private Integer tripsCount;
    private Double avgCargo;
    private Double avgDistance;
    private Double avgFuelConsumption;
    private Double cargoEfficiency;
    private Double cargoToFuelRatio;

    public AdvisorReport(Vehicle vehicle) {
        this.vehicle = vehicle;
        List<Trip> trips = vehicle.getTrips();
        this.tripsCount = trips.size();
        try {
            this.avgCargo = trips.stream().mapToDouble(Trip::getCargo).average().orElse(0d);
            this.avgDistance = trips.stream().mapToDouble(Trip::getDistance).average().orElse(0d);
        } catch (Exception e) {
            e.printStackTrace();
        }
        this.avgFuelConsumption = avgDistance * .01d * vehicle.getLitresper100();

        this.cargoEfficiency =
            (this.avgCargo / vehicle.getCapacity()) *
            (this.avgDistance / vehicle.getRange());
        this.cargoToFuelRatio =
            trips.stream().mapToDouble(Trip::getCargo).sum() /
            (trips.stream().mapToDouble(Trip::getDistance).sum() * .01d * vehicle.getLitresper100());
    }
}
