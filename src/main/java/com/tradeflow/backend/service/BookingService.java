package com.tradeflow.backend.service;

import com.tradeflow.backend.entity.Booking;
import com.tradeflow.backend.entity.User;
import com.tradeflow.backend.exception.BadRequestException;
import com.tradeflow.backend.exception.ResourceNotFoundException;
import com.tradeflow.backend.repository.BookingRepository;
import com.tradeflow.backend.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class BookingService {

    private final BookingRepository bookingRepository;
    private final UserRepository userRepository;
    private final WalletService walletService;

    // Pricing rates per kg by transport mode
    private static final BigDecimal RATE_MARITIME = new BigDecimal("0.50");
    private static final BigDecimal RATE_AIR = new BigDecimal("4.50");
    private static final BigDecimal RATE_LAND = new BigDecimal("1.20");
    private static final BigDecimal RATE_RAIL = new BigDecimal("0.80");
    private static final BigDecimal BASE_FEE = new BigDecimal("150.00");

    public BigDecimal calculatePrice(BigDecimal weightKg, Booking.TransportMode mode) {
        BigDecimal rate = switch (mode) {
            case Maritime -> RATE_MARITIME;
            case Air -> RATE_AIR;
            case Land -> RATE_LAND;
            case Rail -> RATE_RAIL;
        };
        return BASE_FEE.add(weightKg.multiply(rate)).setScale(2, java.math.RoundingMode.HALF_UP);
    }

    public int estimateTransitDays(Booking.TransportMode mode) {
        return switch (mode) {
            case Maritime -> 28;
            case Air -> 3;
            case Land -> 10;
            case Rail -> 15;
        };
    }

    @Transactional
    public Booking createBooking(Long userId, Booking booking) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new BadRequestException("User not found"));

        // Calculate price
        BigDecimal price = calculatePrice(booking.getWeightKg(), booking.getTransportMode());
        int transitDays = estimateTransitDays(booking.getTransportMode());

        // Generate booking number
        String bookingNumber = "BK-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();

        booking.setBookingNumber(bookingNumber);
        booking.setUser(user);
        booking.setPrice(price);
        booking.setStatus(Booking.BookingStatus.PENDING);
        booking.setPaid(false);

        if (booking.getPickupDate() == null) {
            booking.setPickupDate(LocalDate.now().plusDays(2));
        }
        booking.setEstimatedDelivery(booking.getPickupDate().plusDays(transitDays));

        return bookingRepository.save(booking);
    }

    @Transactional
    public Booking payForBooking(Long userId, Long bookingId) {
        Booking booking = bookingRepository.findById(bookingId)
                .orElseThrow(() -> new ResourceNotFoundException("Booking", "id", bookingId));

        if (!booking.getUser().getId().equals(userId)) {
            throw new BadRequestException("This booking does not belong to you");
        }
        if (booking.getPaid()) {
            throw new BadRequestException("Booking is already paid");
        }
        if (booking.getStatus() == Booking.BookingStatus.CANCELLED) {
            throw new BadRequestException("Cannot pay for a cancelled booking");
        }

        // Debit wallet
        walletService.debit(userId, booking.getPrice(),
                "Payment for booking " + booking.getBookingNumber(),
                "BOOKING", booking.getId());

        booking.setPaid(true);
        booking.setStatus(Booking.BookingStatus.CONFIRMED);
        return bookingRepository.save(booking);
    }

    @Transactional
    public Booking cancelBooking(Long userId, Long bookingId) {
        Booking booking = bookingRepository.findById(bookingId)
                .orElseThrow(() -> new ResourceNotFoundException("Booking", "id", bookingId));

        if (!booking.getUser().getId().equals(userId)) {
            throw new BadRequestException("This booking does not belong to you");
        }
        if (booking.getStatus() == Booking.BookingStatus.DELIVERED) {
            throw new BadRequestException("Cannot cancel a delivered booking");
        }
        if (booking.getStatus() == Booking.BookingStatus.CANCELLED) {
            throw new BadRequestException("Booking is already cancelled");
        }

        // Refund if paid
        if (booking.getPaid()) {
            walletService.refund(userId, booking.getPrice(),
                    "Refund for cancelled booking " + booking.getBookingNumber(),
                    "BOOKING_REFUND", booking.getId());
            booking.setPaid(false);
        }

        booking.setStatus(Booking.BookingStatus.CANCELLED);
        return bookingRepository.save(booking);
    }

    @Transactional(readOnly = true)
    public Page<Booking> getUserBookings(Long userId, Pageable pageable) {
        return bookingRepository.findByUserIdOrderByCreatedAtDesc(userId, pageable);
    }

    @Transactional(readOnly = true)
    public Booking getBookingById(Long id) {
        return bookingRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Booking", "id", id));
    }

    @Transactional(readOnly = true)
    public Page<Booking> getAllBookings(Pageable pageable) {
        return bookingRepository.findAll(pageable);
    }

    @Transactional
    public Booking updateBookingStatus(Long bookingId, Booking.BookingStatus status) {
        Booking booking = getBookingById(bookingId);
        booking.setStatus(status);
        if (status == Booking.BookingStatus.DELIVERED) {
            booking.setActualDelivery(LocalDate.now());
        }
        return bookingRepository.save(booking);
    }
}
