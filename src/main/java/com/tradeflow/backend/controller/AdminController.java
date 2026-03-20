package com.tradeflow.backend.controller;

import com.tradeflow.backend.dto.ApiResponse;
import com.tradeflow.backend.entity.*;
import com.tradeflow.backend.repository.*;
import com.tradeflow.backend.service.*;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.CacheManager;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/admin")
@PreAuthorize("hasRole('ADMIN')")
@RequiredArgsConstructor
public class AdminController {

    private final ProductService productService;
    private final ShipmentService shipmentService;
    private final InquiryService inquiryService;
    private final BookingService bookingService;
    private final UserRepository userRepository;
    private final SiteSettingRepository siteSettingRepo;
    private final CacheManager cacheManager;

    // ─── Dashboard Stats ──────────────────────────────────

    @GetMapping("/dashboard")
    public ResponseEntity<ApiResponse<Map<String, Object>>> getDashboard() {
        Map<String, Object> stats = Map.of(
            "totalUsers", userRepository.count(),
            "totalProducts", productService.getAllActiveProducts(Pageable.unpaged()).getTotalElements(),
            "totalShipments", shipmentService.getAllShipments(Pageable.unpaged()).getTotalElements(),
            "totalInquiries", inquiryService.getAllInquiries(Pageable.unpaged()).getTotalElements()
        );
        return ResponseEntity.ok(ApiResponse.success(stats));
    }

    // ─── Product Management ───────────────────────────────

    @PostMapping("/products")
    public ResponseEntity<ApiResponse<Product>> createProduct(@RequestBody Product product) {
        Product saved = productService.createProduct(product);
        return ResponseEntity.status(HttpStatus.CREATED).body(ApiResponse.success(saved));
    }

    @PutMapping("/products/{id}")
    public ResponseEntity<ApiResponse<Product>> updateProduct(@PathVariable Long id, @RequestBody Product product) {
        Product updated = productService.updateProduct(id, product);
        return ResponseEntity.ok(ApiResponse.success(updated));
    }

    @DeleteMapping("/products/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteProduct(@PathVariable Long id) {
        productService.deleteProduct(id);
        return ResponseEntity.ok(ApiResponse.success("Product deactivated", null));
    }

    // ─── Shipment Management ──────────────────────────────

    @GetMapping("/shipments")
    public ResponseEntity<ApiResponse<Page<Shipment>>> getAllShipments(
            @PageableDefault(size = 20) Pageable pageable) {
        return ResponseEntity.ok(ApiResponse.success(shipmentService.getAllShipments(pageable)));
    }

    @PostMapping("/shipments")
    public ResponseEntity<ApiResponse<Shipment>> createShipment(@RequestBody Shipment shipment) {
        Shipment saved = shipmentService.createShipment(shipment);
        return ResponseEntity.status(HttpStatus.CREATED).body(ApiResponse.success(saved));
    }

    @PutMapping("/shipments/{id}")
    public ResponseEntity<ApiResponse<Shipment>> updateShipment(@PathVariable Long id, @RequestBody Shipment shipment) {
        Shipment updated = shipmentService.updateShipment(id, shipment);
        return ResponseEntity.ok(ApiResponse.success(updated));
    }

    // ─── Inquiry Management ───────────────────────────────

    @GetMapping("/inquiries")
    public ResponseEntity<ApiResponse<Page<Inquiry>>> getAllInquiries(
            @RequestParam(required = false) Inquiry.InquiryStatus status,
            @PageableDefault(size = 20) Pageable pageable) {
        Page<Inquiry> inquiries;
        if (status != null) {
            inquiries = inquiryService.getInquiriesByStatus(status, pageable);
        } else {
            inquiries = inquiryService.getAllInquiries(pageable);
        }
        return ResponseEntity.ok(ApiResponse.success(inquiries));
    }

    @PutMapping("/inquiries/{id}/status")
    public ResponseEntity<ApiResponse<Inquiry>> updateInquiryStatus(
            @PathVariable Long id,
            @RequestParam Inquiry.InquiryStatus status,
            @RequestParam(required = false) String adminNotes) {
        Inquiry updated = inquiryService.updateInquiryStatus(id, status, adminNotes);
        return ResponseEntity.ok(ApiResponse.success(updated));
    }

    // ─── User Management ──────────────────────────────────

    @GetMapping("/users")
    public ResponseEntity<ApiResponse<Page<User>>> getAllUsers(@PageableDefault(size = 20) Pageable pageable) {
        return ResponseEntity.ok(ApiResponse.success(userRepository.findAll(pageable)));
    }

    // ─── Site Settings ────────────────────────────────────

    @PutMapping("/site-settings/{key}")
    public ResponseEntity<ApiResponse<SiteSetting>> updateSetting(
            @PathVariable String key, @RequestBody Map<String, String> body) {
        SiteSetting setting = siteSettingRepo.findBySettingKey(key)
                .orElseThrow(() -> new RuntimeException("Setting not found: " + key));
        setting.setSettingValue(body.get("value"));
        siteSettingRepo.save(setting);
        return ResponseEntity.ok(ApiResponse.success(setting));
    }

    // ─── Booking Management ─────────────────────────────────

    @GetMapping("/bookings")
    public ResponseEntity<ApiResponse<Page<Booking>>> getAllBookings(
            @PageableDefault(size = 20) Pageable pageable) {
        return ResponseEntity.ok(ApiResponse.success(bookingService.getAllBookings(pageable)));
    }

    @PutMapping("/bookings/{id}/status")
    public ResponseEntity<ApiResponse<Booking>> updateBookingStatus(
            @PathVariable Long id,
            @RequestParam Booking.BookingStatus status) {
        Booking updated = bookingService.updateBookingStatus(id, status);
        return ResponseEntity.ok(ApiResponse.success(updated));
    }

    // ─── Cache Management ─────────────────────────────────

    @PostMapping("/cache/clear")
    public ResponseEntity<ApiResponse<Void>> clearAllCaches() {
        cacheManager.getCacheNames().forEach(name -> {
            var cache = cacheManager.getCache(name);
            if (cache != null) cache.clear();
        });
        return ResponseEntity.ok(ApiResponse.success("All caches cleared", null));
    }
}
