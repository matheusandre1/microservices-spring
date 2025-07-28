package com.algaworks.algadelivery.delivery.tracking.domain.repository;

import com.algaworks.algadelivery.delivery.tracking.domain.model.ContactPoint;
import com.algaworks.algadelivery.delivery.tracking.domain.model.Delivery;
import com.algaworks.algadelivery.delivery.tracking.domain.model.DeliveryStatus;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.AutoConfigureDataJpa;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.math.BigDecimal;
import java.time.Duration;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class DeliveryRepositoryTest {

    @Autowired
    private DeliveryRepository deliveryRepository;

    @Test
    void shouldPersistDelivery() {


        Delivery delivery = Delivery.draft();

        delivery.addItem("Notebook", 1);
        delivery.addItem("Mouse", 2);
        delivery.addItem("Teclado", 1);

        delivery.editPreparationDetails(createdValidPreparationDetails());

        deliveryRepository.save(delivery);

        Delivery savedDelivery = deliveryRepository.findById(delivery.getId()).orElseThrow();

        assertEquals(3, savedDelivery.getItems().size());
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