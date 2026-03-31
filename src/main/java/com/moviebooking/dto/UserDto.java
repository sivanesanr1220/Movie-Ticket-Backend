package com.moviebooking.dto;

import com.moviebooking.model.User;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.time.LocalDateTime;

// All DTOs for the User Module in one file
public class UserDto {

    // ── What client sends to UPDATE profile ──────────────────
    @Data
    public static class UpdateProfileRequest {
        @NotBlank(message = "Name cannot be blank")
        private String name;

        private String phone;
    }

    // ── What client sends to CHANGE PASSWORD ─────────────────
    @Data
    public static class ChangePasswordRequest {
        @NotBlank(message = "Old password is required")
        private String oldPassword;

        @NotBlank(message = "New password is required")
        @Size(min = 6, message = "New password must be at least 6 characters")
        private String newPassword;
    }

    // ── What we send BACK as user profile ────────────────────
    @Data
    public static class UserProfileResponse {
        private Long id;
        private String name;
        private String email;
        private String phone;
        private String role;
        private boolean active;
        private LocalDateTime createdAt;

        // Factory method — convert entity to DTO
        public static UserProfileResponse from(User user) {
            UserProfileResponse res = new UserProfileResponse();
            res.setId(user.getId());
            res.setName(user.getName());
            res.setEmail(user.getEmail());
            res.setPhone(user.getPhone());
            res.setRole(user.getRole().name());
            res.setActive(user.isActive());
            res.setCreatedAt(user.getCreatedAt());
            return res;
        }
    }

    // ── Admin: create user manually ───────────────────────────
    @Data
    public static class AdminCreateUserRequest {
        @NotBlank
        private String name;

        @Email
        @NotBlank
        private String email;

        @NotBlank
        @Size(min = 6)
        private String password;

        private String phone;

        private User.Role role;  // ADMIN can assign any role
    }
}
