package com.tradeflow.backend.service;

import com.tradeflow.backend.entity.User;
import com.tradeflow.backend.entity.Wallet;
import com.tradeflow.backend.entity.WalletTransaction;
import com.tradeflow.backend.exception.BadRequestException;
import com.tradeflow.backend.repository.UserRepository;
import com.tradeflow.backend.repository.WalletRepository;
import com.tradeflow.backend.repository.WalletTransactionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

@Service
@RequiredArgsConstructor
public class WalletService {

    private final WalletRepository walletRepository;
    private final WalletTransactionRepository transactionRepository;
    private final UserRepository userRepository;

    @Transactional(readOnly = true)
    public Wallet getWallet(Long userId) {
        return walletRepository.findByUserId(userId)
                .orElseGet(() -> createWallet(userId));
    }

    @Transactional
    public Wallet createWallet(Long userId) {
        if (walletRepository.findByUserId(userId).isPresent()) {
            return walletRepository.findByUserId(userId).get();
        }
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new BadRequestException("User not found"));

        Wallet wallet = Wallet.builder()
                .user(user)
                .balance(BigDecimal.ZERO)
                .build();
        return walletRepository.save(wallet);
    }

    @Transactional
    public Wallet addFunds(Long userId, BigDecimal amount, String description) {
        if (amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new BadRequestException("Amount must be positive");
        }
        if (amount.compareTo(new BigDecimal("1000000")) > 0) {
            throw new BadRequestException("Maximum top-up is $1,000,000");
        }

        Wallet wallet = getWallet(userId);
        BigDecimal newBalance = wallet.getBalance().add(amount);
        wallet.setBalance(newBalance);
        walletRepository.save(wallet);

        WalletTransaction txn = WalletTransaction.builder()
                .wallet(wallet)
                .type(WalletTransaction.TransactionType.CREDIT)
                .amount(amount)
                .balanceAfter(newBalance)
                .description(description)
                .referenceType("TOP_UP")
                .build();
        transactionRepository.save(txn);

        return wallet;
    }

    @Transactional
    public Wallet debit(Long userId, BigDecimal amount, String description, String refType, Long refId) {
        if (amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new BadRequestException("Amount must be positive");
        }

        Wallet wallet = getWallet(userId);
        if (wallet.getBalance().compareTo(amount) < 0) {
            throw new BadRequestException("Insufficient balance. Current: $" +
                    wallet.getBalance().toPlainString() + ", Required: $" + amount.toPlainString());
        }

        BigDecimal newBalance = wallet.getBalance().subtract(amount);
        wallet.setBalance(newBalance);
        walletRepository.save(wallet);

        WalletTransaction txn = WalletTransaction.builder()
                .wallet(wallet)
                .type(WalletTransaction.TransactionType.DEBIT)
                .amount(amount)
                .balanceAfter(newBalance)
                .description(description)
                .referenceType(refType)
                .referenceId(refId)
                .build();
        transactionRepository.save(txn);

        return wallet;
    }

    @Transactional
    public Wallet refund(Long userId, BigDecimal amount, String description, String refType, Long refId) {
        Wallet wallet = getWallet(userId);
        BigDecimal newBalance = wallet.getBalance().add(amount);
        wallet.setBalance(newBalance);
        walletRepository.save(wallet);

        WalletTransaction txn = WalletTransaction.builder()
                .wallet(wallet)
                .type(WalletTransaction.TransactionType.CREDIT)
                .amount(amount)
                .balanceAfter(newBalance)
                .description(description)
                .referenceType(refType)
                .referenceId(refId)
                .build();
        transactionRepository.save(txn);

        return wallet;
    }

    @Transactional(readOnly = true)
    public Page<WalletTransaction> getTransactions(Long userId, Pageable pageable) {
        Wallet wallet = getWallet(userId);
        return transactionRepository.findByWalletIdOrderByCreatedAtDesc(wallet.getId(), pageable);
    }
}
