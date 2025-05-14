package com.example.leapit.user;

import com.example.leapit.common.enums.Role;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;

public class UserResponse {
    @Data
    public static class TokenDTO {
        private String accessToken;

        @Builder
        public TokenDTO(String accessToken) {
            this.accessToken = accessToken;
        }
    }

    @Data
    public static class DTO {
        private Integer id;
        private String username;
        private String email;
        private Role role;
        private String createdAt;

        public DTO(User user) {
            this.id = user.getId();
            this.username = user.getUsername();
            this.email = user.getEmail();
            this.role = user.getRole();
            this.createdAt = user.getCreatedAt().toString();
        }
    }

    @Data
    public static class UpdateDTO {
        private Integer id;
        private String username;
        private String email;
        private Role role;
        private String createdAt;
        private String name;
        private LocalDate birthDate;

        public UpdateDTO(User user) {
            this.id = user.getId();
            this.username = user.getUsername();
            this.email = user.getEmail();
            this.role = user.getRole();
            this.createdAt = user.getCreatedAt().toString();
            this.name = user.getName();
            this.birthDate = user.getBirthDate();
        }
    }
}
