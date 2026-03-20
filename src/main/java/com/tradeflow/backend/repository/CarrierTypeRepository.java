package com.tradeflow.backend.repository;

import com.tradeflow.backend.entity.CarrierType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CarrierTypeRepository extends JpaRepository<CarrierType, Long> {

    Optional<CarrierType> findBySlug(String slug);
}
