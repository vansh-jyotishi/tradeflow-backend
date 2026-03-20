package com.tradeflow.backend.repository;

import com.tradeflow.backend.entity.Booking;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface BookingRepository extends JpaRepository<Booking, Long> {

    Page<Booking> findByUserIdOrderByCreatedAtDesc(Long userId, Pageable pageable);

    Optional<Booking> findByBookingNumber(String bookingNumber);

    Page<Booking> findByStatus(Booking.BookingStatus status, Pageable pageable);
}
