package com.tradeflow.backend.controller;

import com.tradeflow.backend.dto.ApiResponse;
import com.tradeflow.backend.entity.Shipment;
import com.tradeflow.backend.security.CustomUserDetails;
import com.tradeflow.backend.service.ShipmentService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/shipments")
@RequiredArgsConstructor
public class ShipmentController {

    private final ShipmentService shipmentService;

    @GetMapping("/track/{trackingId}")
    public ResponseEntity<ApiResponse<Shipment>> trackShipment(@PathVariable String trackingId) {
        Shipment shipment = shipmentService.trackShipment(trackingId);
        return ResponseEntity.ok(ApiResponse.success(shipment));
    }

    @GetMapping("/my")
    public ResponseEntity<ApiResponse<Page<Shipment>>> getMyShipments(
            @AuthenticationPrincipal CustomUserDetails user,
            @PageableDefault(size = 10) Pageable pageable) {
        Page<Shipment> shipments = shipmentService.getUserShipments(user.getId(), pageable);
        return ResponseEntity.ok(ApiResponse.success(shipments));
    }
}
