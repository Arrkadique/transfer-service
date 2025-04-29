package com.arrkadique.transferservice.service;

import com.arrkadique.transferservice.dto.response.UserResponse;
import com.arrkadique.transferservice.entity.EmailData;
import com.arrkadique.transferservice.entity.PhoneData;
import com.arrkadique.transferservice.entity.User;
import com.arrkadique.transferservice.mapper.UserMapper;
import com.arrkadique.transferservice.repository.EmailDataRepository;
import com.arrkadique.transferservice.repository.PhoneDataRepository;
import com.arrkadique.transferservice.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service
@Slf4j
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final EmailDataRepository emailDataRepository;
    private final PhoneDataRepository phoneDataRepository;
    private final UserMapper userMapper;

    public void updateEmail(Long userId, String newEmail) {
        if (emailDataRepository.existsByEmailAndUserIdNot(newEmail, userId)) {
            throw new IllegalArgumentException("Email is already in use");
        }

        if (!userRepository.existsById(userId)) {
            throw new IllegalArgumentException("User not found");
        }

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

    @Cacheable(value = "userSearchCache", key = "#name + '_' + #phone + '_' + #email + '_' + #dob + '_' + #pageable.pageNumber + '_' + #pageable.pageSize")
    public Page<UserResponse> searchUsers(LocalDate dob, String phone, String email, String name, Pageable pageable) {
        log.debug("Cache MISS: Fetching users from DB");
        Page<User> users = userRepository.searchUsers(dob, phone, email, name, pageable);
        return users.map(userMapper::toDto);
    }
}
