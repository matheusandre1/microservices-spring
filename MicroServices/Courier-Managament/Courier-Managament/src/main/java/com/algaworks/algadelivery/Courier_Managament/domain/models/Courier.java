package com.algaworks.algadelivery.Courier_Managament.domain.models;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import lombok.*;

import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

@Entity
@Getter
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@NoArgsConstructor(access = AccessLevel.PACKAGE)
@Setter(AccessLevel.PRIVATE)
public class Courier {

    @Id
    @EqualsAndHashCode.Include
    private UUID id;

    @Setter(AccessLevel.PUBLIC)
    private String name;

    @Setter(AccessLevel.PUBLIC)
    private String phone;

    private Integer fullfilledDeliveriesQuantity;
    private Integer pendingDeliveriesQuantity;

    private OffsetDateTime lastFulfilledAt;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true, mappedBy = "courier")
    private List<AssignedDelivery> pendingDeliveries = new ArrayList<>();


    public List<AssignedDelivery> getPendingDeliveries(){
        return Collections.unmodifiableList(this.pendingDeliveries);}


    public static Courier brandNew(String name, String phone) {
        Courier courier = new Courier();
        courier.setId(UUID.randomUUID());
        courier.setName(name);
        courier.setPhone(phone);
        courier.setFullfilledDeliveriesQuantity(0);
        courier.setPendingDeliveriesQuantity(0);
        return courier;
    }

    public void  assing(UUID id)
    {
        this.pendingDeliveries.add(AssignedDelivery.pending(id, this));
        this.pendingDeliveriesQuantity++;
    }

    public void fulfill(UUID id)
    {
        AssignedDelivery assignedDelivery = this.pendingDeliveries.stream()
                .filter(assigned -> assigned.getId().equals(id))
                .findFirst()
                .orElseThrow();

        this.pendingDeliveries.remove(assignedDelivery);
        this.pendingDeliveriesQuantity--;
        this.fullfilledDeliveriesQuantity++;
        this.lastFulfilledAt = OffsetDateTime.now();
    }



}
