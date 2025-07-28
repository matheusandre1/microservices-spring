package com.algaworks.algadelivery.delivery.tracking.domain.model;

import com.algaworks.algadelivery.delivery.tracking.domain.exception.DomainException;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.Duration;

import static org.junit.jupiter.api.Assertions.*;

class DeliveryTest {

    @Test
    void shouldChangeStatustoPlace() {
        Delivery delivery = Delivery.draft();

        delivery.editPreparationDetails(createdValidPreparationDetails());

        delivery.place();

        assertEquals(DeliveryStatus.WAITING_FOR_COURIER, delivery.getStatus());
        assertNotNull(delivery.getPlaceAt());
    }


    @Test
    void shouldNotChangeStatustoPlace() {
        Delivery delivery = Delivery.draft();


        assertThrows(DomainException.class, () -> delivery.place());

        assertEquals(DeliveryStatus.DRAFT, delivery.getStatus());
        assertNull(delivery.getPlaceAt());
    }

    private Delivery.PreparationDetails createdValidPreparationDetails()
    {
       ContactPoint sender = ContactPoint.builder()
               .zipCode("0000-000")
               .street("Rua São Paulo")
               .number("200")
               .complement("Apto 101")
                .name("João da Silva")
                .phone("11999999999")
               .build();
       ContactPoint recipient = ContactPoint.builder()
                .zipCode("1111-111")
                .street("Rua Rio de Janeiro")
                .number("300")
                .complement("Casa 2")
                .name("Maria de Souza")
                .phone("11888888888")
               .build();

         return Delivery.PreparationDetails.builder()
                 .sender(sender)
                 .recipient(recipient)
                 .distanceFee(new BigDecimal("15.00"))
                 .courierPayout(new BigDecimal("10.00"))
                 .expectedDeliveryTime(Duration.ofHours(5))
                 .build();


    }

}