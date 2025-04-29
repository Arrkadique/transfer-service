package com.arrkadique.transferservice.service;

import com.arrkadique.transferservice.entity.EmailData;
import com.arrkadique.transferservice.entity.PhoneData;
import com.arrkadique.transferservice.entity.User;
import com.arrkadique.transferservice.repository.EmailDataRepository;
import com.arrkadique.transferservice.repository.PhoneDataRepository;
import com.arrkadique.transferservice.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final EmailDataRepository emailDataRepository;
    private final PhoneDataRepository phoneDataRepository;

    public void updateEmail(Long userId, String newEmail) {
        if (emailDataRepository.existsByEmailAndUserIdNot(newEmail, userId)) {
            throw new IllegalArgumentException("Email is already in use");
        }

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("User not found"));

        EmailData emailData = emailDataRepository.findAll().stream()
                .filter(e -> e.getUser().getId().equals(userId)).findFirst()
                .orElseGet(() -> {
                    EmailData newData = new EmailData();
                    newData.setUser(userRepository.findById(userId).orElseThrow());
                    newData.setEmail(newEmail);
                    return newData;
                });


        emailData.setEmail(newEmail);
        emailDataRepository.save(emailData);
    }

    public void updatePhone(Long userId, String newPhone) {
        if (phoneDataRepository.existsByPhoneAndUserIdNot(newPhone, userId)) {
            throw new IllegalArgumentException("Phone is already in use");
        }

        PhoneData phone = phoneDataRepository.findAll().stream()
                .filter(p -> p.getUser().getId().equals(userId)).findFirst()
                .orElseGet(() -> {
                    PhoneData newData = new PhoneData();
                    newData.setUser(userRepository.findById(userId).orElseThrow());
                    return newData;
                });

        phone.setPhone(newPhone);
        phoneDataRepository.save(phone);
    }

    public Page<User> searchUsers(LocalDate dob, String phone, String email, String name, Pageable pageable) {
        return userRepository.searchUsers(dob, phone, email, name, pageable);
    }
}
