package com.tradeflow.backend.repository;

import com.tradeflow.backend.entity.Inquiry;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface InquiryRepository extends JpaRepository<Inquiry, Long> {

    Page<Inquiry> findByStatus(Inquiry.InquiryStatus status, Pageable pageable);

    Page<Inquiry> findByUserId(Long userId, Pageable pageable);

    Page<Inquiry> findAllByOrderByCreatedAtDesc(Pageable pageable);
}
