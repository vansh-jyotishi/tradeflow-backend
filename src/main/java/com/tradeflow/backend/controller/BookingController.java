package com.tradeflow.backend.controller;

import com.tradeflow.backend.dto.ApiResponse;
import com.tradeflow.backend.entity.Booking;
import com.tradeflow.backend.security.CustomUserDetails;
import com.tradeflow.backend.service.BookingService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.Map;

@RestController
@RequestMapping("/api/bookings")
@RequiredArgsConstructor
public class BookingController {

    private final BookingService bookingService;

    @PostMapping
    public ResponseEntity<ApiResponse<Booking>> createBooking(
            @AuthenticationPrincipal CustomUserDetails user,
            @RequestBody Booking booking) {
        Booking saved = bookingService.createBooking(user.getId(), booking);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success("Booking created. Please pay to confirm.", saved));
    }

    @PostMapping("/{id}/pay")
    public ResponseEntity<ApiResponse<Booking>> payBooking(
            @AuthenticationPrincipal CustomUserDetails user,
            @PathVariable Long id) {
        Booking paid = bookingService.payForBooking(user.getId(), id);
        return ResponseEntity.ok(ApiResponse.success("Booking paid and confirmed!", paid));
    }

    @PostMapping("/{id}/cancel")
    public ResponseEntity<ApiResponse<Booking>> cancelBooking(
            @AuthenticationPrincipal CustomUserDetails user,
            @PathVariable Long id) {
        Booking cancelled = bookingService.cancelBooking(user.getId(), id);
        return ResponseEntity.ok(ApiResponse.success("Booking cancelled. Refund processed if paid.", cancelled));
    }

    @GetMapping
    public ResponseEntity<ApiResponse<Page<Booking>>> getMyBookings(
            @AuthenticationPrincipal CustomUserDetails user,
            @PageableDefault(size = 10) Pageable pageable) {
        Page<Booking> bookings = bookingService.getUserBookings(user.getId(), pageable);
        return ResponseEntity.ok(ApiResponse.success(bookings));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<Booking>> getBooking(
            @AuthenticationPrincipal CustomUserDetails user,
            @PathVariable Long id) {
        Booking booking = bookingService.getBookingById(id);
        return ResponseEntity.ok(ApiResponse.success(booking));
    }

    @GetMapping("/estimate")
    public ResponseEntity<ApiResponse<Map<String, Object>>> getEstimate(
            @RequestParam BigDecimal weightKg,
            @RequestParam Booking.TransportMode mode) {
        BigDecimal price = bookingService.calculatePrice(weightKg, mode);
        int days = bookingService.estimateTransitDays(mode);
        Map<String, Object> estimate = Map.of(
                "price", price,
                "currency", "USD",
                "estimatedDays", days,
                "weightKg", weightKg,
                "transportMode", mode
        );
        return ResponseEntity.ok(ApiResponse.success(estimate));
    }
}
