package com.arrkadique.transferservice.service;

import com.arrkadique.transferservice.entity.EmailData;
import com.arrkadique.transferservice.entity.PhoneData;
import com.arrkadique.transferservice.entity.User;
import com.arrkadique.transferservice.repository.EmailDataRepository;
import com.arrkadique.transferservice.repository.PhoneDataRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class AuthenticationService {

    private final BCryptPasswordEncoder passwordEncoder;
    private final PhoneDataRepository phoneDataRepository;
    private final EmailDataRepository emailDataRepository;


    public User authenticate(String identifier, String rawPassword) {
        Optional<User> user = emailDataRepository.findByEmail(identifier)
                .map(EmailData::getUser);

        if (user.isEmpty()) {
            user = phoneDataRepository.findByPhone(identifier)
                    .map(PhoneData::getUser);
        }

        User authenticated = user.filter(u -> passwordEncoder.matches(rawPassword, u.getPassword())).orElseThrow(
                () -> new BadCredentialsException("Invalid identifier or password")
        );
        log.info("Authenticated user {} with id {}", authenticated.getName(), authenticated.getId());

        return authenticated;
    }
}