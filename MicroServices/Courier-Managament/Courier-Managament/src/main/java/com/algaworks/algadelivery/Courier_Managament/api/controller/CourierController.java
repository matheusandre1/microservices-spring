package com.algaworks.algadelivery.Courier_Managament.api.controller;

import com.algaworks.algadelivery.Courier_Managament.api.model.CourierInput;
import com.algaworks.algadelivery.Courier_Managament.domain.models.Courier;
import com.algaworks.algadelivery.Courier_Managament.domain.repository.CourierRepository;
import com.algaworks.algadelivery.Courier_Managament.domain.service.CourierService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.web.PageableDefault;
import org.springframework.data.web.PagedModel;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.awt.print.Pageable;
import java.util.UUID;

@RestController
@RequestMapping("/couriers")
@RequiredArgsConstructor
public class CourierController {

    private final CourierService courierService;
    private final CourierRepository courierRepository;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Courier registerCourier(@RequestBody @Valid CourierInput courierInput) {
        return courierService.create(courierInput);
    }

    @PutMapping("/{courierId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public Courier updateCourier(@PathVariable UUID courierId, @RequestBody @Valid CourierInput courierInput) {
        return courierService.update(courierId, courierInput);
    }

    @GetMapping
    public PagedModel<Courier> findAll(@PageableDefault Pageable pageable) {
        return new PagedModel<>(courierRepository.findAll((org.springframework.data.domain.Pageable) pageable));
    }

    @GetMapping("/{courierId}")
    public Courier findById(@PathVariable UUID courierId) {
        return courierRepository.findById(courierId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Courier not found"));
    }
}
