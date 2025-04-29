package com.arrkadique.transferservice.controller;

import com.arrkadique.transferservice.dto.response.UserResponse;
import com.arrkadique.transferservice.entity.User;
import com.arrkadique.transferservice.service.UserService;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/user")
public class UserController {

    private final UserService userService;

    @PutMapping("/email")
    public ResponseEntity<Void> updateEmail(@RequestParam @Email String email, Authentication auth) {
        User user = (User) auth.getPrincipal();
        userService.updateEmail(user.getId(), email);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/phone")
    public ResponseEntity<Void> updatePhone(@RequestParam @NotBlank String phone, Authentication auth) {
        User user = (User) auth.getPrincipal();
        userService.updatePhone(user.getId(), phone);
        return ResponseEntity.ok().build();
    }

    @GetMapping
    public Page<UserResponse> searchUsers(
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dateOfBirth,
            @RequestParam(required = false) String phone,
            @RequestParam(required = false) String email,
            @RequestParam(required = false, defaultValue = "") String name,
            @PageableDefault(size = 5) Pageable pageable
    ) {
        log.info("API call: searchUsers | name={}, phone={}, email={}, dateOfBirth={}", name, phone, email, dateOfBirth);

        Page<UserResponse> result = userService.searchUsers(dateOfBirth, phone, email, name, pageable);

        log.info("API response: {} users found", result.getTotalElements());
        return result;
    }
}