package com.tradeflow.backend.repository;

import com.tradeflow.backend.entity.Banner;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;

public interface BannerRepository extends JpaRepository<Banner, Long> {

    @Query("SELECT b FROM Banner b WHERE b.isActive = true AND b.position = :position " +
           "AND (b.startsAt IS NULL OR b.startsAt <= :now) " +
           "AND (b.endsAt IS NULL OR b.endsAt >= :now) " +
           "ORDER BY b.sortOrder")
    List<Banner> findActiveBanners(@Param("position") String position, @Param("now") LocalDateTime now);
}
