package com.algaworks.algadelivery.delivery.tracking.infra.http.client;

import com.algaworks.algadelivery.delivery.tracking.domain.service.CourierPayoutCalculationService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
@RequiredArgsConstructor
public class CourierPayoutCalculationServiceHttpImp implements CourierPayoutCalculationService {

    private final CourierApiClient courierApiClient;
    @Override
    public BigDecimal calculatePayout(Double distanceInKm) {

        var courierPayoutResultModel = courierApiClient.calculatePayout(new CourierPayoutCalculationInput(distanceInKm));

        return courierPayoutResultModel.getPayoutFee();
    }
}
