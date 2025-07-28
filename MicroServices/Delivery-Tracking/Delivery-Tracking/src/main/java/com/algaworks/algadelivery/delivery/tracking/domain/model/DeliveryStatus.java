package com.algaworks.algadelivery.delivery.tracking.domain.model;

import java.util.Arrays;
import java.util.List;

public enum DeliveryStatus {
    DRAFT,
    WAITING_FOR_COURIER(DRAFT),
    IN_TRANSIT(WAITING_FOR_COURIER),
    DELIVERED(IN_TRANSIT);

    private final List<DeliveryStatus> previewStatuses;

    DeliveryStatus(DeliveryStatus... previewStatuses) {
        this.previewStatuses = Arrays.asList(previewStatuses);
    }

    public boolean canNotChangeTo(DeliveryStatus newStatus) {
        DeliveryStatus currentStatus = this;
        return !newStatus.previewStatuses.contains(currentStatus);
    }
    public boolean canChangeTo(DeliveryStatus newStatus) {
        return !canNotChangeTo(newStatus);
    }

}
