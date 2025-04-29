package com.arrkadique.transferservice.scheduled;

import com.arrkadique.transferservice.entity.Account;
import com.arrkadique.transferservice.repository.AccountRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
@RequiredArgsConstructor
public class BalanceUpdateScheduler {

    private final AccountRepository accountRepository;

    @Value("${app.deposit.max-percentage}")
    private float maxPercentage;

    @Value("${app.deposit.increased-percentage}")
    private float increasePercentage;

    @Transactional
    @Scheduled(fixedRateString = "${app.deposit.update-period}", fixedDelayString = "${app.deposit.update-delay}")
    public void increaseBalances() {
        List<Account> accounts = accountRepository.findAllForUpdate();
        for (Account account : accounts) {
            BigDecimal current = account.getBalance();
            BigDecimal initial = account.getInitialBalance();

            BigDecimal max = initial.multiply(BigDecimal.valueOf(maxPercentage));
            BigDecimal increased = current.multiply(BigDecimal.valueOf(increasePercentage));

            if (increased.compareTo(max) > 0) {
                increased = max;
            }

            account.setBalance(increased);
        }

        accountRepository.saveAll(accounts);
    }
}