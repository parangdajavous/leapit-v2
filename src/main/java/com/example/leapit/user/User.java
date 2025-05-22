package com.example.leapit.user;

import com.example.leapit.common.enums.Role;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.sql.Timestamp;
import java.time.LocalDate;

@NoArgsConstructor
@Getter
@Table(name = "user_tb")
@Entity
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(unique = true, nullable = false)
    private String username;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false)
    private String email;

    @Column(nullable = false)
    private String contactNumber;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Role role; // PERSONAL / COMPANY

    @CreationTimestamp
    private Timestamp createdAt;

    // --- 개인 유저일 경우 ---
    private String name;
    private LocalDate birthDate;

    @Builder
    public User(Integer id, String username, String password, String email, String contactNumber, Role role, Timestamp createdAt, String name, LocalDate birthDate) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.email = email;
        this.contactNumber = contactNumber;
        this.role = role;
        this.createdAt = createdAt;
        this.name = name;
        this.birthDate = birthDate;
    }

    public void companyUpdate(UserRequest.CompanyUpdateDTO reqDTO) {
        this.email = reqDTO.getEmail();
        this.password = reqDTO.getNewPassword();
        this.contactNumber = reqDTO.getContactNumber();
    }

    public void personalUpdate(UserRequest.PersonalUpdateDTO reqDTO) {
        this.name = reqDTO.getName();
        this.password = reqDTO.getNewPassword();
        this.email = reqDTO.getEmail();
        this.contactNumber = reqDTO.getContactNumber();
    }
}