package com.arrkadique.transferservice.repository;

import com.arrkadique.transferservice.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long>, UserSearchRepository {
}