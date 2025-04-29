package com.arrkadique.transferservice.service;

import com.arrkadique.transferservice.entity.Account;
import com.arrkadique.transferservice.entity.User;
import com.arrkadique.transferservice.repository.AccountRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class TransferServiceApplicationTest {

    @Mock
    private AccountRepository accountRepository;

    @InjectMocks
    private TransferService transferService;

    @Test
    void testSuccessfulTransfer() {
        var fromUserId = 1L;
        var toUserId = 2L;
        var amount = new BigDecimal("100.00");

        var fromAccount = new Account();
        User user1 = User.builder().id(fromUserId).build();
        fromAccount.setBalance(new BigDecimal("500.00"));
        fromAccount.setUser(user1);

        var toAccount = new Account();
        User user2 = User.builder().id(toUserId).build();
        toAccount.setBalance(new BigDecimal("300.00"));
        toAccount.setUser(user2);

        when(accountRepository.findByUserIdForUpdate(fromUserId)).thenReturn(Optional.of(fromAccount));
        when(accountRepository.findByUserIdForUpdate(toUserId)).thenReturn(Optional.of(toAccount));

        transferService.transfer(fromUserId, toUserId, amount);

        assertEquals(new BigDecimal("400.00"), fromAccount.getBalance());
        assertEquals(new BigDecimal("400.00"), toAccount.getBalance());
    }

    @Test
    void testTransferToSelfThrowsException() {
        var userId = 1L;
        var amount = new BigDecimal("50");

        assertThrows(IllegalArgumentException.class, () ->
                transferService.transfer(userId, userId, amount)
        );
    }

    @Test
    void testTransferInsufficientFunds() {
        var fromUserId = 1L;
        var toUserId = 2L;
        var amount = new BigDecimal("600.00");

        var fromAccount = new Account();
        User user1 = User.builder().id(fromUserId).build();
        fromAccount.setBalance(new BigDecimal("500.00"));
        fromAccount.setUser(user1);

        var toAccount = new Account();
        User user2 = User.builder().id(toUserId).build();
        toAccount.setBalance(new BigDecimal("0.00"));
        toAccount.setUser(user2);

        when(accountRepository.findByUserIdForUpdate(fromUserId)).thenReturn(Optional.of(fromAccount));
        when(accountRepository.findByUserIdForUpdate(toUserId)).thenReturn(Optional.of(toAccount));

        assertThrows(IllegalStateException.class, () ->
                transferService.transfer(fromUserId, toUserId, amount)
        );
    }

}
