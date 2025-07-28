package com.algaworks.algadelivery.delivery.tracking.infra;

import com.algaworks.algadelivery.delivery.tracking.domain.model.ContactPoint;
import com.algaworks.algadelivery.delivery.tracking.domain.service.DeliveryTimeEstimate;
import com.algaworks.algadelivery.delivery.tracking.domain.service.DeliveryTimeEstimationService;
import org.springframework.stereotype.Service;

import java.time.Duration;

@Service
public class DeliveryEstimationServiceIMP implements DeliveryTimeEstimationService {
    @Override
    public DeliveryTimeEstimate estimate(ContactPoint sender, ContactPoint receiver) {
        return new DeliveryTimeEstimate(Duration.ofHours(3),3.1);
    }
}
