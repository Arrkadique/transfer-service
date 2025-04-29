package com.arrkadique.transferservice.service;

import com.arrkadique.transferservice.entity.Account;
import com.arrkadique.transferservice.repository.AccountRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
@RequiredArgsConstructor
public class TransferService {

    private final AccountRepository accountRepository;

    @Transactional
    public void transfer(Long fromUserId, Long toUserId, BigDecimal amount) {
        if (fromUserId.equals(toUserId)) {
            throw new IllegalArgumentException("You cannot transfer to the same user");
        }

        if (amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Sum must be greater than zero");
        }

        Account fromAccount = accountRepository.findByUserIdForUpdate(fromUserId)
            .orElseThrow(() -> new EntityNotFoundException("Sender account not found"));

        Account toAccount = accountRepository.findByUserIdForUpdate(toUserId)
            .orElseThrow(() -> new EntityNotFoundException("Account not found"));

        if (fromAccount.getBalance().compareTo(amount) < 0) {
            throw new IllegalStateException("Not enough balance");
        }

        fromAccount.setBalance(fromAccount.getBalance().subtract(amount));
        toAccount.setBalance(toAccount.getBalance().add(amount));
    }
}