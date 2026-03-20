package com.tradeflow.backend.repository;

import com.tradeflow.backend.entity.RevenueByRegion;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface RevenueByRegionRepository extends JpaRepository<RevenueByRegion, Long> {

    List<RevenueByRegion> findByYear(Integer year);
}
