package com.algaworks.algadelivery.delivery.tracking.domain.service;

import com.algaworks.algadelivery.delivery.tracking.api.model.ContactPointInput;
import com.algaworks.algadelivery.delivery.tracking.api.model.DeliveryInput;
import com.algaworks.algadelivery.delivery.tracking.api.model.ItemInput;
import com.algaworks.algadelivery.delivery.tracking.domain.exception.DomainException;
import com.algaworks.algadelivery.delivery.tracking.domain.model.ContactPoint;
import com.algaworks.algadelivery.delivery.tracking.domain.model.Delivery;
import com.algaworks.algadelivery.delivery.tracking.domain.repository.DeliveryRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.Duration;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class DeliveryService {

    private DeliveryRepository deliveryRepository;

    @Transactional
    public Delivery draft(DeliveryInput deliveryInput) {

        Delivery delivery = Delivery.draft();
        handlePreparation(deliveryInput, delivery);

                // Logic to create a new delivery
        return deliveryRepository.saveAndFlush(delivery);
    }

    @Transactional
    public Delivery edit(UUID deliveryId, DeliveryInput deliveryInput) {

        Delivery delivery = deliveryRepository.findById(deliveryId)
                .orElseThrow(() -> new DomainException("Delivery not found"));

        handlePreparation(deliveryInput, delivery);

        delivery.removeItems();
        // Logic to update an existing delivery
        return deliveryRepository.saveAndFlush(delivery);
    }


    private void handlePreparation(DeliveryInput deliveryInput, Delivery delivery)
    {
        ContactPointInput senderInput = deliveryInput.getSender();
        ContactPointInput recipientInput = deliveryInput.getRecipient();

        ContactPoint senderContactPoint = ContactPoint.builder()
                .phone(senderInput.getPhone())
                .name(senderInput.getName())
                .complement(senderInput.getComplement())
                .number(senderInput.getNumber())
                .zipCode(senderInput.getZipCode())
                .street(senderInput.getStreet())
                .build();

        ContactPoint recipientContactPoint = ContactPoint.builder()
                .phone(recipientInput.getPhone())
                .name(recipientInput.getName())
                .complement(recipientInput.getComplement())
                .number(recipientInput.getNumber())
                .zipCode(recipientInput.getZipCode())
                .street(recipientInput.getStreet())
                .build();

        Duration expectedDeliveryTime = Duration.ofHours(3);
        BigDecimal payout = new BigDecimal("'10");

        var preparationDetails = Delivery.PreparationDetails.builder()
                .recipient(recipientContactPoint)
                .sender(senderContactPoint)
                .courierPayout(payout)
                .expectedDeliveryTime(expectedDeliveryTime)
                .distanceFee(new BigDecimal("10"))
                .build();

        delivery.editPreparationDetails(preparationDetails);

        for (ItemInput item : deliveryInput.getItems())
        {
            delivery.addItem(item.getName(), item.getQuantity());

        }


    }


}
