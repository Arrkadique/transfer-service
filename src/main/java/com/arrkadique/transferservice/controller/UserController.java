package com.arrkadique.transferservice.controller;

import com.arrkadique.transferservice.entity.User;
import com.arrkadique.transferservice.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/user")
public class UserController {

    private final UserService userService;

    @PutMapping("/email")
    public void updateEmail(@RequestParam String email, Authentication auth) {
        Long userId = Long.parseLong(auth.getName());
        userService.updateEmail(userId, email);
    }

    @PutMapping("/phone")
    public void updatePhone(@RequestParam String phone, Authentication auth) {
        Long userId = Long.parseLong(auth.getName());
        userService.updatePhone(userId, phone);
    }

    @GetMapping
    public Page<User> searchUsers(
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dateOfBirth,
            @RequestParam(required = false) String phone,
            @RequestParam(required = false) String email,
            @RequestParam(required = false) String name,
            @PageableDefault(size = 10) Pageable pageable
    ) {
        return userService.searchUsers(dateOfBirth, phone, email, name, pageable);
    }
}