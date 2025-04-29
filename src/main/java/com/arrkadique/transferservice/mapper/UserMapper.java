package com.arrkadique.transferservice.mapper;

import com.arrkadique.transferservice.dto.response.UserResponse;
import com.arrkadique.transferservice.entity.EmailData;
import com.arrkadique.transferservice.entity.PhoneData;
import com.arrkadique.transferservice.entity.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface UserMapper {

    @Mapping(source = "account.id", target = "accountId")
    @Mapping(source = "dateOfBirth", target = "dateOfBirth", dateFormat = "yyyy-MM-dd")
    @Mapping(target = "phones", expression = "java(mapPhones(user.getPhones()))")
    @Mapping(target = "emails", expression = "java(mapEmails(user.getEmails()))")
    UserResponse toDto(User user);

    default List<String> mapPhones(List<PhoneData> phones) {
        return phones == null ? null : phones.stream()
                .map(PhoneData::getPhone)
                .toList();
    }

    default List<String> mapEmails(List<EmailData> emails) {
        return emails == null ? null : emails.stream()
                .map(EmailData::getEmail)
                .toList();
    }
}