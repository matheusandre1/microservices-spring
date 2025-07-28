package com.algaworks.algadelivery.delivery.tracking.infra.http.client;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.service.annotation.HttpExchange;

@HttpExchange("/couriers")
public interface CourierApiClient {

    @PostMapping("/payout-calculation")
    CourierPayoutResultModel calculatePayout(@RequestBody  CourierPayoutCalculationInput payoutRequestModel);

}

