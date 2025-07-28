package com.algaworks.algadelivery.delivery.tracking.api.controller;

import com.algaworks.algadelivery.delivery.tracking.api.model.CourierId;
import com.algaworks.algadelivery.delivery.tracking.api.model.DeliveryInput;
import com.algaworks.algadelivery.delivery.tracking.domain.model.Delivery;
import com.algaworks.algadelivery.delivery.tracking.domain.repository.DeliveryRepository;
import com.algaworks.algadelivery.delivery.tracking.domain.service.DeliveryCheckPointService;
import com.algaworks.algadelivery.delivery.tracking.domain.service.DeliveryService;
import jakarta.validation.Valid;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.web.PageableDefault;
import org.springframework.data.web.PagedModel;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.awt.print.Pageable;
import java.util.UUID;

@RestController
@RequestMapping("/deliveries")
@RequiredArgsConstructor
public class DeliveryController {

    private final DeliveryService deliveryService;
    private final DeliveryRepository deliveryRepository;
    private final DeliveryCheckPointService deliveryCheckPointService;




    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Delivery draft(@RequestBody @Valid DeliveryInput delivery) {

        return deliveryService.draft(delivery);

    }

    @PutMapping("/{deliveryId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public Delivery edit(@PathVariable UUID deliveryId, @RequestBody @Valid DeliveryInput delivery) {

        return deliveryService.edit(deliveryId, delivery);
    }

    @GetMapping
    public PagedModel<Delivery> findAll(@PageableDefault Pageable pageable) {
        return new PagedModel<>(deliveryRepository.findAll((org.springframework.data.domain.Pageable) pageable));
    }

    @GetMapping("/{deliveryId}")
    public Delivery findById(@PathVariable UUID deliveryId) {
        return deliveryRepository.findById(deliveryId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
    }

    @PostMapping("/{deliveryId}/placement")
    public void place(@PathVariable UUID deliveryId) {
        deliveryCheckPointService.place(deliveryId);
    }


    @PostMapping("/{deliveryId}/pickups")
    public void pickUp(@PathVariable UUID deliveryId, @Valid @RequestBody CourierId courierId) {
        deliveryCheckPointService.pickUp(deliveryId, courierId.getCourerId());
    }
    @PostMapping("/{deliveryId}/completion")
    public void completion(@PathVariable UUID deliveryId) {
        deliveryCheckPointService.completation(deliveryId);
    }

}
