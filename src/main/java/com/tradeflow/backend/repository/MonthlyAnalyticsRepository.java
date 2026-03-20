package com.tradeflow.backend.repository;

import com.tradeflow.backend.entity.MonthlyAnalytics;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MonthlyAnalyticsRepository extends JpaRepository<MonthlyAnalytics, Long> {

    List<MonthlyAnalytics> findByYearOrderByMonth(Integer year);

    List<MonthlyAnalytics> findByYearInOrderByYearAscMonthAsc(List<Integer> years);
}
