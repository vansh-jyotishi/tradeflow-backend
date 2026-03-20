package com.tradeflow.backend.repository;

import com.tradeflow.backend.entity.Shipment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ShipmentRepository extends JpaRepository<Shipment, Long> {

    Optional<Shipment> findByTrackingId(String trackingId);

    Page<Shipment> findByUserId(Long userId, Pageable pageable);

    Page<Shipment> findByStatusId(Long statusId, Pageable pageable);
}
