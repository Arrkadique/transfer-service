package com.arrkadique.transferservice.scheduled;

import com.arrkadique.transferservice.entity.Account;
import com.arrkadique.transferservice.repository.AccountRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class BalanceUpdateScheduler {

    private final AccountRepository accountRepository;

    @Value("${app.deposit.max-percentage}")
    private float maxPercentage;

    @Value("${app.deposit.increased-percentage}")
    private float increasePercentage;

    @Transactional
    @Scheduled(fixedRateString = "${app.deposit.update-period}", initialDelayString = "${app.deposit.update-delay}")
    public void increaseBalances() {
        log.info("Scheduled task started: increaseBalances");
        long start = System.currentTimeMillis();
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
        log.info("Scheduled task completed in {} ms", System.currentTimeMillis() - start);
    }
}