package com.tradeflow.backend.repository;

import com.tradeflow.backend.entity.PlatformStat;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface PlatformStatRepository extends JpaRepository<PlatformStat, Long> {

    Optional<PlatformStat> findByStatKey(String statKey);

    List<PlatformStat> findAllByOrderBySortOrder();
}
