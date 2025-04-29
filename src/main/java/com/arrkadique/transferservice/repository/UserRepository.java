package com.arrkadique.transferservice.repository;

import com.arrkadique.transferservice.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;

public interface UserRepository extends JpaRepository<User, Long> {
    @Query("""
        SELECT DISTINCT u FROM User u
        LEFT JOIN u.emails e
        LEFT JOIN u.phones p
        WHERE (:dob IS NULL OR u.dateOfBirth > :dob)
          AND (:phone IS NULL OR p.phone = :phone)
          AND (:email IS NULL OR e.email = :email)
          AND (:name IS NULL OR LOWER(u.name) LIKE LOWER(CONCAT('%', :name, '%')))
        """)
    Page<User> searchUsers(@Param("dob") LocalDate dob,
                           @Param("phone") String phone,
                           @Param("email") String email,
                           @Param("name") String name,
                           Pageable pageable);
}