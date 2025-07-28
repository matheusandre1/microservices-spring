package com.algaworks.algadelivery.delivery.tracking.domain.service;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.time.Duration;

@Getter
@AllArgsConstructor
public class DeliveryTimeEstimate {

    private Duration estimatedTime;
    private Double estimatedDistanceInKm;
}
