package com.tradeflow.backend.repository;

import com.tradeflow.backend.entity.Carrier;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface CarrierRepository extends JpaRepository<Carrier, Long> {

    Optional<Carrier> findBySlug(String slug);

    List<Carrier> findByIsActiveTrueOrderBySortOrder();

    Page<Carrier> findByCarrierTypeIdAndIsActiveTrue(Long carrierTypeId, Pageable pageable);
}
