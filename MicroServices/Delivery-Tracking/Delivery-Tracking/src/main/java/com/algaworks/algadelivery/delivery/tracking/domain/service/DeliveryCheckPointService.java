package com.algaworks.algadelivery.delivery.tracking.domain.service;


import com.algaworks.algadelivery.delivery.tracking.domain.exception.DomainException;
import com.algaworks.algadelivery.delivery.tracking.domain.repository.DeliveryRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@Transactional
@RequiredArgsConstructor
public class DeliveryCheckPointService {

    private final DeliveryRepository deliveryRepository;

    public void place(UUID deliveryId) {
        var delivery = deliveryRepository.findById(deliveryId)
                .orElseThrow(() -> new DomainException("Delivery not found"));

                delivery.place();
        deliveryRepository.saveAndFlush(delivery);
    }

    public void pickUp(UUID deliveryId, UUID courierId) {
        var delivery = deliveryRepository.findById(deliveryId)
                .orElseThrow(() -> new DomainException("Delivery not found"));

        delivery.pickUp(courierId);
        deliveryRepository.saveAndFlush(delivery);
    }

    public void completation(UUID deliveryId) {
        var delivery = deliveryRepository.findById(deliveryId)
                .orElseThrow(() -> new DomainException("Delivery not found"));

        delivery.markAsDelivered();
        deliveryRepository.saveAndFlush(delivery);
    }
}
