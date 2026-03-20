package com.tradeflow.backend.controller;

import com.tradeflow.backend.dto.ApiResponse;
import com.tradeflow.backend.entity.*;
import com.tradeflow.backend.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class SiteContentController {

    private final SiteSettingRepository siteSettingRepo;
    private final BannerRepository bannerRepo;
    private final TrustedPartnerRepository partnerRepo;
    private final TradeRouteRepository tradeRouteRepo;
    private final GlobeCityMarkerRepository globeMarkerRepo;

    @GetMapping("/site-settings")
    @Cacheable("siteSettings")
    public ResponseEntity<ApiResponse<List<SiteSetting>>> getAllSettings() {
        return ResponseEntity.ok(ApiResponse.success(siteSettingRepo.findAll()));
    }

    @GetMapping("/site-settings/{key}")
    public ResponseEntity<ApiResponse<SiteSetting>> getSetting(@PathVariable String key) {
        return siteSettingRepo.findBySettingKey(key)
                .map(s -> ResponseEntity.ok(ApiResponse.success(s)))
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/banners")
    public ResponseEntity<ApiResponse<List<Banner>>> getBanners(
            @RequestParam(defaultValue = "hero") String position) {
        List<Banner> banners = bannerRepo.findActiveBanners(position, LocalDateTime.now());
        return ResponseEntity.ok(ApiResponse.success(banners));
    }

    @GetMapping("/partners")
    @Cacheable("trustedPartners")
    public ResponseEntity<ApiResponse<List<TrustedPartner>>> getPartners() {
        return ResponseEntity.ok(ApiResponse.success(partnerRepo.findByIsActiveTrueOrderBySortOrder()));
    }

    @GetMapping("/trade-routes")
    @Cacheable("tradeRoutes")
    public ResponseEntity<ApiResponse<List<TradeRoute>>> getTradeRoutes() {
        return ResponseEntity.ok(ApiResponse.success(tradeRouteRepo.findByIsActiveTrueOrderBySortOrder()));
    }

    @GetMapping("/globe-markers")
    @Cacheable("globeMarkers")
    public ResponseEntity<ApiResponse<List<GlobeCityMarker>>> getGlobeMarkers() {
        return ResponseEntity.ok(ApiResponse.success(globeMarkerRepo.findByIsActiveTrue()));
    }
}
