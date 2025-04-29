package com.arrkadique.transferservice.scheduled;

import com.arrkadique.transferservice.entity.Account;
import com.arrkadique.transferservice.repository.AccountRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
@RequiredArgsConstructor
public class BalanceUpdateService {

    private final AccountRepository accountRepository;

    @Transactional
    @Scheduled(fixedRate = 30000, initialDelay = 5000)
    public void increaseBalances() {
        List<Account> accounts = accountRepository.findAllForUpdate();
        for (Account account : accounts) {
            BigDecimal current = account.getBalance();
            BigDecimal initial = account.getInitialBalance();

            BigDecimal max = initial.multiply(BigDecimal.valueOf(2.07)); // 207%
            BigDecimal increased = current.multiply(BigDecimal.valueOf(1.10)); // +10%

            if (increased.compareTo(max) > 0) {
                increased = max;
            }

            account.setBalance(increased);
        }

        accountRepository.saveAll(accounts);
    }
}