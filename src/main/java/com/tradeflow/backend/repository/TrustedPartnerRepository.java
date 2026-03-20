package com.tradeflow.backend.repository;

import com.tradeflow.backend.entity.TrustedPartner;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TrustedPartnerRepository extends JpaRepository<TrustedPartner, Long> {

    List<TrustedPartner> findByIsActiveTrueOrderBySortOrder();
}
