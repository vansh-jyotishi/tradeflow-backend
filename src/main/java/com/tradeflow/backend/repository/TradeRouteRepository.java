package com.tradeflow.backend.repository;

import com.tradeflow.backend.entity.TradeRoute;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TradeRouteRepository extends JpaRepository<TradeRoute, Long> {

    List<TradeRoute> findByIsActiveTrueOrderBySortOrder();
}
