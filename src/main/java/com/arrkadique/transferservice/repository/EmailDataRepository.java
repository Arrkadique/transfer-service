package com.arrkadique.transferservice.repository;

import com.arrkadique.transferservice.entity.EmailData;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface EmailDataRepository extends JpaRepository<EmailData, Long> {
    Optional<EmailData> findByEmail(String email);

    boolean existsByEmailAndUserIdNot(String newEmail, Long userId);
}