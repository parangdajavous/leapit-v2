package com.example.leapit.user;

import com.example.leapit.common.enums.Role;
import lombok.Data;

public class UserResponse {
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
}
