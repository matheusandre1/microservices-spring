package com.algaworks.algadelivery.Courier_Managament.domain.models;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import lombok.*;

import java.time.OffsetDateTime;
import java.util.UUID;

@Entity
@Getter
@Setter(AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PACKAGE)
@EqualsAndHashCode
public class AssignedDelivery {

    @Id
    @EqualsAndHashCode.Include
    private UUID id;
    private OffsetDateTime assignedAt;

    @ManyToOne(optional = false)
    @Getter(AccessLevel.PRIVATE)
    private Courier courier;

    static AssignedDelivery pending(UUID deliveryId, Courier courier)
    {
        AssignedDelivery assignedDelivery = new AssignedDelivery();
        assignedDelivery.setId(deliveryId);
        assignedDelivery.setAssignedAt(OffsetDateTime.now());
        assignedDelivery.setCourier(courier);
        return assignedDelivery;
    }
}
