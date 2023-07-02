package com.spedhelper.spedhelper.entities;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Vehicle {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private @ManyToOne Customer owner;
    private @JsonIgnore @OneToMany(mappedBy = "vehicle", cascade = CascadeType.ALL, orphanRemoval = true) List<Trip> trips;
    private String model = "";
    private Double capacity = 0d;
    private Double range = 0d;
    private Double litresper100 = 0d;
    private Double odometer = 0d;
    
    @Override
    public String toString() {
        return "Vehicle{id=" + this.id +
            ", model=" + this.model +
            ", capacity=" + this.capacity +
            "kg, L/100km =" + this.litresper100 +
            ", odometer=" + this.odometer + "km}";
    }
}