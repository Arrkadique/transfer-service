package com.arrkadique.transferservice.controller;

import com.arrkadique.transferservice.dto.request.TransferRequest;
import com.arrkadique.transferservice.entity.User;
import com.arrkadique.transferservice.service.TransferService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/transfer")
public class TransferController {

    private final TransferService transferService;

    @PostMapping
    public ResponseEntity<?> transfer(
            Authentication auth,
            @Valid @RequestBody TransferRequest request) {
        User user = (User) auth.getPrincipal();
        transferService.transfer(user.getId(), request.getToUserId(), request.getAmount());
        return ResponseEntity.ok("Transferred successfully");
    }
}