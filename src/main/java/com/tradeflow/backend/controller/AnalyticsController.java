package com.tradeflow.backend.controller;

import com.tradeflow.backend.dto.ApiResponse;
import com.tradeflow.backend.entity.*;
import com.tradeflow.backend.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/analytics")
@RequiredArgsConstructor
public class AnalyticsController {

    private final MonthlyAnalyticsRepository monthlyAnalyticsRepo;
    private final RevenueByRegionRepository revenueByRegionRepo;
    private final PlatformStatRepository platformStatRepo;

    @GetMapping("/monthly")
    @Cacheable(value = "monthlyAnalytics", key = "#year")
    public ResponseEntity<ApiResponse<List<MonthlyAnalytics>>> getMonthlyAnalytics(
            @RequestParam(defaultValue = "2026") Integer year) {
        return ResponseEntity.ok(ApiResponse.success(monthlyAnalyticsRepo.findByYearOrderByMonth(year)));
    }

    @GetMapping("/monthly/compare")
    public ResponseEntity<ApiResponse<List<MonthlyAnalytics>>> compareYears(
            @RequestParam List<Integer> years) {
        return ResponseEntity.ok(ApiResponse.success(monthlyAnalyticsRepo.findByYearInOrderByYearAscMonthAsc(years)));
    }

    @GetMapping("/revenue-by-region")
    @Cacheable(value = "revenueByRegion", key = "#year")
    public ResponseEntity<ApiResponse<List<RevenueByRegion>>> getRevenueByRegion(
            @RequestParam(defaultValue = "2026") Integer year) {
        return ResponseEntity.ok(ApiResponse.success(revenueByRegionRepo.findByYear(year)));
    }

    @GetMapping("/platform-stats")
    @Cacheable("platformStats")
    public ResponseEntity<ApiResponse<List<PlatformStat>>> getPlatformStats() {
        return ResponseEntity.ok(ApiResponse.success(platformStatRepo.findAllByOrderBySortOrder()));
    }
}
