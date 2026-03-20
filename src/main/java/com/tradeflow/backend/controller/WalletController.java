package com.tradeflow.backend.controller;

import com.tradeflow.backend.dto.ApiResponse;
import com.tradeflow.backend.entity.Wallet;
import com.tradeflow.backend.entity.WalletTransaction;
import com.tradeflow.backend.security.CustomUserDetails;
import com.tradeflow.backend.service.WalletService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.Map;

@RestController
@RequestMapping("/api/wallet")
@RequiredArgsConstructor
public class WalletController {

    private final WalletService walletService;

    @GetMapping
    public ResponseEntity<ApiResponse<Wallet>> getWallet(@AuthenticationPrincipal CustomUserDetails user) {
        Wallet wallet = walletService.getWallet(user.getId());
        return ResponseEntity.ok(ApiResponse.success(wallet));
    }

    @PostMapping("/topup")
    public ResponseEntity<ApiResponse<Wallet>> topUp(
            @AuthenticationPrincipal CustomUserDetails user,
            @RequestBody Map<String, String> body) {
        BigDecimal amount = new BigDecimal(body.get("amount"));
        String description = body.getOrDefault("description", "Wallet top-up");
        Wallet wallet = walletService.addFunds(user.getId(), amount, description);
        return ResponseEntity.ok(ApiResponse.success("Funds added successfully", wallet));
    }

    @GetMapping("/transactions")
    public ResponseEntity<ApiResponse<Page<WalletTransaction>>> getTransactions(
            @AuthenticationPrincipal CustomUserDetails user,
            @PageableDefault(size = 20) Pageable pageable) {
        Page<WalletTransaction> txns = walletService.getTransactions(user.getId(), pageable);
        return ResponseEntity.ok(ApiResponse.success(txns));
    }
}
