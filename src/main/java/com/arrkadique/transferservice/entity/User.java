package com.arrkadique.transferservice.entity;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

import java.time.LocalDate;
import java.util.List;

@Entity
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @Column(length = 500)
    String name;

    @Column(name = "date_of_birth")
    LocalDate dateOfBirth;

    @Column(length = 500)
    String password;

    @OneToOne(mappedBy = "user", cascade = CascadeType.ALL)
    Account account;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    List<EmailData> emails;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    List<PhoneData> phones;

}