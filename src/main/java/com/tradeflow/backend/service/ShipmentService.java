package com.tradeflow.backend.service;

import com.tradeflow.backend.entity.Shipment;
import com.tradeflow.backend.exception.ResourceNotFoundException;
import com.tradeflow.backend.repository.ShipmentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ShipmentService {

    private final ShipmentRepository shipmentRepository;

    @Transactional(readOnly = true)
    public Shipment trackShipment(String trackingId) {
        return shipmentRepository.findByTrackingId(trackingId)
                .orElseThrow(() -> new ResourceNotFoundException("Shipment", "trackingId", trackingId));
    }

    @Transactional(readOnly = true)
    public Page<Shipment> getUserShipments(Long userId, Pageable pageable) {
        return shipmentRepository.findByUserId(userId, pageable);
    }

    @Transactional(readOnly = true)
    public Page<Shipment> getAllShipments(Pageable pageable) {
        return shipmentRepository.findAll(pageable);
    }

    @Transactional
    public Shipment createShipment(Shipment shipment) {
        return shipmentRepository.save(shipment);
    }

    @Transactional
    public Shipment updateShipment(Long id, Shipment updated) {
        Shipment existing = shipmentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Shipment", "id", id));
        existing.setStatus(updated.getStatus());
        existing.setEta(updated.getEta());
        existing.setDeliveredDate(updated.getDeliveredDate());
        existing.setNotes(updated.getNotes());
        return shipmentRepository.save(existing);
    }
}
