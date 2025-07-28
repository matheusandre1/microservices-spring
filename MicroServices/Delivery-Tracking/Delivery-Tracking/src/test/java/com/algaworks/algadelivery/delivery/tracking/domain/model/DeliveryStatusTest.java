package com.algaworks.algadelivery.delivery.tracking.domain.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class DeliveryStatusTest {
    @Test
    void canChangeToDraft() {
        assertTrue(DeliveryStatus.DRAFT.canChangeTo(DeliveryStatus.WAITING_FOR_COURIER));
    }

    @Test
    void canNotChangeToDraft() {
        assertTrue(DeliveryStatus.DRAFT.canNotChangeTo(DeliveryStatus.DRAFT));
    }
}