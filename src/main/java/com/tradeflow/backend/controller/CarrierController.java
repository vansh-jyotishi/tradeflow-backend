package com.tradeflow.backend.controller;

import com.tradeflow.backend.dto.ApiResponse;
import com.tradeflow.backend.entity.Carrier;
import com.tradeflow.backend.entity.CarrierType;
import com.tradeflow.backend.exception.ResourceNotFoundException;
import com.tradeflow.backend.repository.CarrierRepository;
import com.tradeflow.backend.repository.CarrierTypeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/carriers")
@RequiredArgsConstructor
public class CarrierController {

    private final CarrierRepository carrierRepository;
    private final CarrierTypeRepository carrierTypeRepository;

    @GetMapping
    public ResponseEntity<ApiResponse<List<Carrier>>> getCarriers(
            @RequestParam(required = false) Long typeId) {
        List<Carrier> carriers;
        if (typeId != null) {
            carriers = carrierRepository.findByCarrierTypeIdAndIsActiveTrue(typeId, Pageable.unpaged()).getContent();
        } else {
            carriers = carrierRepository.findByIsActiveTrueOrderBySortOrder();
        }
        return ResponseEntity.ok(ApiResponse.success(carriers));
    }

    @GetMapping("/types")
    public ResponseEntity<ApiResponse<List<CarrierType>>> getCarrierTypes() {
        return ResponseEntity.ok(ApiResponse.success(carrierTypeRepository.findAll()));
    }

    @GetMapping("/{slug}")
    public ResponseEntity<ApiResponse<Carrier>> getCarrier(@PathVariable String slug) {
        Carrier carrier = carrierRepository.findBySlug(slug)
                .orElseThrow(() -> new ResourceNotFoundException("Carrier", "slug", slug));
        return ResponseEntity.ok(ApiResponse.success(carrier));
    }
}
