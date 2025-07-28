package com.algaworks.algadelivery.delivery.tracking.infra.http.client;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.support.RestClientAdapter;
import org.springframework.web.service.invoker.HttpServiceProxyFactory;

@Configuration
public class CourierApiClientConfig {

    @Bean
    public CourierApiClient courierApiClient(RestClient.Builder builder) {

        RestClient restClient = builder.baseUrl("http://localhost:8081").build();

        var adapter = RestClientAdapter.create(restClient);

        HttpServiceProxyFactory factory = HttpServiceProxyFactory
                .builderFor(adapter)
                .build();


        return factory.createClient(CourierApiClient.class);

    }
}
