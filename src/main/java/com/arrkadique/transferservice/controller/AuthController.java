package com.arrkadique.transferservice.controller;

import com.arrkadique.transferservice.dto.request.AuthRequest;
import com.arrkadique.transferservice.dto.response.TokenResponse;
import com.arrkadique.transferservice.jwt.JwtUtil;
import com.arrkadique.transferservice.service.AuthenticationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthenticationService authService;
    private final JwtUtil jwtUtil;

    @PostMapping("/login")
    public ResponseEntity<TokenResponse> login(@RequestBody AuthRequest request) {
        TokenResponse response = new TokenResponse(
                jwtUtil.generateToken(
                        authService.authenticate(request.getIdentifier(), request.getPassword()).getId()));
        return ResponseEntity.ok(response);
    }
}