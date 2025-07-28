package com.algaworks.algadelivery.delivery.tracking.domain.model;

import com.algaworks.algadelivery.delivery.tracking.domain.exception.DomainException;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.Duration;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

@Entity

@NoArgsConstructor(access = AccessLevel.PACKAGE)
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@Setter(AccessLevel.PRIVATE)
@Getter
public class Delivery {

    @Id
    @EqualsAndHashCode.Include
    private UUID id;
    private UUID courierId;

    private DeliveryStatus status;

    private OffsetDateTime placeAt;
    private OffsetDateTime assingedAt;
    private OffsetDateTime expectedDeliveryAt;
    private OffsetDateTime fullfilledAt;

    private BigDecimal distanceFee;
    private BigDecimal courierPayout;
    private BigDecimal totalCost;

    private Integer totalItems;

    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name = "zipCode", column = @Column(name = "sender_zip_code")),
            @AttributeOverride(name = "street", column = @Column(name = "sender_street")),
            @AttributeOverride(name = "number", column = @Column(name = "sender_number")),
            @AttributeOverride(name = "complement", column = @Column(name = "sender_complement")),
            @AttributeOverride(name = "name", column = @Column(name = "sender_name")),
            @AttributeOverride(name = "phone", column = @Column(name = "sender_phone"))
    })
    private ContactPoint sender;
    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name = "zipCode", column = @Column(name = "recipient_zip_code")),
            @AttributeOverride(name = "street", column = @Column(name = "recipient_street")),
            @AttributeOverride(name = "number", column = @Column(name = "recipient_number")),
            @AttributeOverride(name = "complement", column = @Column(name = "recipient_complement")),
            @AttributeOverride(name = "name", column = @Column(name = "recipient_name")),
            @AttributeOverride(name = "phone", column = @Column(name = "recipient_phone"))
    })
    private ContactPoint recipient;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true, mappedBy = "delivery")
    private List<Item> items = new ArrayList<>();

    public static Delivery draft()
    {
        Delivery delivery = new Delivery();
        delivery.setId(UUID.randomUUID());
        delivery.setStatus(DeliveryStatus.DRAFT);
        delivery.setPlaceAt(OffsetDateTime.now());
        delivery.setDistanceFee(BigDecimal.ZERO);
        delivery.setCourierPayout(BigDecimal.ZERO);
        delivery.setTotalCost(BigDecimal.ZERO);
        delivery.setTotalItems(0);
        return delivery;
    }

    public UUID addItem(String name, int quantity) {
        Item item = Item.brandNew(name, quantity, this);
        this.items.add(item);
        calculateTotalItems();
        return item.getId();
    }

    public void removeItem(UUID itemId)
    {
        items.removeIf(item -> item.getId().equals(itemId));
        calculateTotalItems();
    }


    public void changeItemQuantity(UUID itemId, int quantity) {
        items.stream()
                .filter(item -> item.getId().equals(itemId))
                .findFirst()
                .ifPresent(item -> item.setQuantity(quantity));
        calculateTotalItems();
    }

    public void removeItems(){
        this.items.clear();
        calculateTotalItems();
    }

    public void editPreparationDetails(PreparationDetails preparationDetails)
    {
           verifyIfCanEdited();
            this.setSender(preparationDetails.getSender());
            this.setRecipient(preparationDetails.getRecipient());
            this.setDistanceFee(preparationDetails.getDistanceFee());
            this.setCourierPayout(preparationDetails.getCourierPayout());

            this.setExpectedDeliveryAt(OffsetDateTime.now().plus(preparationDetails.getExpectedDeliveryTime()));
            this.setTotalCost(this.getDistanceFee().add(this.getCourierPayout()));
    }



    public void place()
    {
        this.changeStatusto(DeliveryStatus.WAITING_FOR_COURIER);
        this.setPlaceAt(OffsetDateTime.now());
    }

    public void pickUp(UUID courierId)
    {
        this.setCourierId(courierId);
        this.changeStatusto(DeliveryStatus.IN_TRANSIT);
        this.setAssingedAt(OffsetDateTime.now());
    }

    public void markAsDelivered()
    {
        this.changeStatusto(DeliveryStatus.DELIVERED);
        this.setFullfilledAt(OffsetDateTime.now());
    }

    public List<Item> getItems() {
        return Collections.unmodifiableList(this.items);
    }

    private void calculateTotalItems()
    {
        int totalItems = this.items.stream()
                .mapToInt(Item::getQuantity)
                .sum();

        setTotalItems(totalItems);
    }


    private void verifyIfCanPlace() {
        if (!isFiller()) {
            throw new DomainException();
        }

        if(!this.getStatus().equals(DeliveryStatus.DRAFT)){
            throw new DomainException();
        }
    }

    private boolean isFiller() {
        return this.getSender() != null &&
                this.getRecipient() != null &&
                this.getTotalCost() != null;
    }

    private void verifyIfCanEdited()
    {
        if (!this.getStatus().equals(DeliveryStatus.DRAFT)) {
            throw new DomainException();
        }
    }

    private void changeStatusto(DeliveryStatus newStatus) {
        if (newStatus != null && this.getStatus().canNotChangeTo(newStatus)) {
            throw new DomainException("Cannot change status from " + this.getStatus() + " to " + newStatus);
        }
        this.setStatus(newStatus);
    }

    @Getter
    @AllArgsConstructor
    @Builder
    public static class PreparationDetails
    {
        private ContactPoint sender;
        private ContactPoint recipient;
        private BigDecimal distanceFee;
        private BigDecimal courierPayout;
        private Duration expectedDeliveryTime;
    }
}
