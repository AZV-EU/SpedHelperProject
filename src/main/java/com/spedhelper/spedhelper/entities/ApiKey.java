package com.spedhelper.spedhelper.entities;

import java.time.ZonedDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class ApiKey {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private @ManyToOne Customer owner;
    private @Column(length = 64) String apiKey;
    private ApiKeyBillingType billingType = ApiKeyBillingType.STANDARD;
    private Long totalRequestsCount = 0l;
    private Long requestsSinceLastBillingCount = 0l;
    private ZonedDateTime lastRequestDate = ZonedDateTime.now();
    private ZonedDateTime lastBillingDate = ZonedDateTime.now();
}