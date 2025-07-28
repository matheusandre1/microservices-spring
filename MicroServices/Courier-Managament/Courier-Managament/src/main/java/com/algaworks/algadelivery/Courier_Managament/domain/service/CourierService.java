package com.algaworks.algadelivery.Courier_Managament.domain.service;

import com.algaworks.algadelivery.Courier_Managament.api.model.CourierInput;
import com.algaworks.algadelivery.Courier_Managament.domain.models.Courier;
import com.algaworks.algadelivery.Courier_Managament.domain.repository.CourierRepository;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional
public class CourierService {
    
    private final CourierRepository courierRepository;


    public Courier create(@Valid CourierInput courierInput)
    {
        Courier courier = Courier.brandNew(courierInput.getName(),courierInput.getPhone());

        return courierRepository.saveAndFlush(courier);
    }

    public Courier update(UUID courierId, @Valid CourierInput courierInput)
    {
        Courier courier = courierRepository.findById(courierId)
                .orElseThrow(() -> new IllegalArgumentException("Courier not found"));

        courier.setName(courierInput.getName());
        courier.setPhone(courierInput.getPhone());
        return courierRepository.saveAndFlush(courier);
    }
}