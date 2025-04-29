package com.arrkadique.transferservice.repository;

import com.arrkadique.transferservice.entity.Account;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AccountRepository extends JpaRepository<Account, Long> {
}
