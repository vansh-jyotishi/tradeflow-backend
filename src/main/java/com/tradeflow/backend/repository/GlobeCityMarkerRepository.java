package com.tradeflow.backend.repository;

import com.tradeflow.backend.entity.GlobeCityMarker;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface GlobeCityMarkerRepository extends JpaRepository<GlobeCityMarker, Long> {

    List<GlobeCityMarker> findByIsActiveTrue();
}
