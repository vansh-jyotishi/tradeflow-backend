package com.tradeflow.backend.service;

import com.tradeflow.backend.entity.Inquiry;
import com.tradeflow.backend.exception.ResourceNotFoundException;
import com.tradeflow.backend.repository.InquiryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class InquiryService {

    private final InquiryRepository inquiryRepository;

    @Transactional
    public Inquiry submitInquiry(Inquiry inquiry) {
        return inquiryRepository.save(inquiry);
    }

    @Transactional(readOnly = true)
    public Page<Inquiry> getAllInquiries(Pageable pageable) {
        return inquiryRepository.findAllByOrderByCreatedAtDesc(pageable);
    }

    @Transactional(readOnly = true)
    public Page<Inquiry> getInquiriesByStatus(Inquiry.InquiryStatus status, Pageable pageable) {
        return inquiryRepository.findByStatus(status, pageable);
    }

    @Transactional(readOnly = true)
    public Inquiry getInquiryById(Long id) {
        return inquiryRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Inquiry", "id", id));
    }

    @Transactional
    public Inquiry updateInquiryStatus(Long id, Inquiry.InquiryStatus status, String adminNotes) {
        Inquiry inquiry = getInquiryById(id);
        inquiry.setStatus(status);
        inquiry.setAdminNotes(adminNotes);
        if (status == Inquiry.InquiryStatus.RESOLVED || status == Inquiry.InquiryStatus.CLOSED) {
            inquiry.setResolvedAt(LocalDateTime.now());
        }
        return inquiryRepository.save(inquiry);
    }
}
