package com.algaworks.algadelivery.Courier_Managament.domain.repository;

import com.algaworks.algadelivery.Courier_Managament.domain.models.Courier;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface CourierRepository extends JpaRepository<Courier, UUID> {
}
