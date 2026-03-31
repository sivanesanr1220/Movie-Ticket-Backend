package com.moviebooking.controller;

import com.moviebooking.dto.ApiResponse;
import com.moviebooking.dto.BookingDto;
import com.moviebooking.dto.UserDto.*;
import com.moviebooking.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

   
    @GetMapping("/api/users/me")
    public ResponseEntity<ApiResponse<UserProfileResponse>> getMyProfile(Authentication auth) {
        return ResponseEntity.ok(
            ApiResponse.success("Profile fetched", userService.getMyProfile(auth.getName()))
        );
    }

    @PutMapping("/api/users/me")
    public ResponseEntity<ApiResponse<UserProfileResponse>> updateMyProfile(
            Authentication auth,
            @Valid @RequestBody UpdateProfileRequest request) {
        return ResponseEntity.ok(
            ApiResponse.success("Profile updated", userService.updateMyProfile(auth.getName(), request))
        );
    }

    @PutMapping("/api/users/me/change-password")
    public ResponseEntity<ApiResponse<Void>> changePassword(
            Authentication auth,
            @Valid @RequestBody ChangePasswordRequest request) {
        userService.changePassword(auth.getName(), request);
        return ResponseEntity.ok(ApiResponse.success("Password changed successfully", null));
    }

    @GetMapping("/api/users/me/bookings")
    public ResponseEntity<ApiResponse<List<BookingDto>>> getMyBookings(Authentication auth) {
        return ResponseEntity.ok(
            ApiResponse.success("Booking history", userService.getMyBookingHistory(auth.getName()))
        );
    }

    // DELETE /api/users/me  → deactivate my own account
    @DeleteMapping("/api/users/me")
    public ResponseEntity<ApiResponse<Void>> deactivateMyAccount(Authentication auth) {
        userService.deactivateMyAccount(auth.getName());
        return ResponseEntity.ok(ApiResponse.success("Account deactivated", null));
    }

  
    @GetMapping("/api/admin/users")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<List<UserProfileResponse>>> getAllUsers() {
        return ResponseEntity.ok(ApiResponse.success("All users", userService.getAllUsers()));
    }

    @GetMapping("/api/admin/users/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<UserProfileResponse>> getUserById(@PathVariable Long id) {
        return ResponseEntity.ok(ApiResponse.success("User found", userService.getUserById(id)));
    }

    // POST /api/admin/users  → admin creates a new user
    @PostMapping("/api/admin/users")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<UserProfileResponse>> createUser(
            @Valid @RequestBody AdminCreateUserRequest request) {
        return ResponseEntity.ok(ApiResponse.success("User created", userService.adminCreateUser(request)));
    }

    @PutMapping("/api/admin/users/{id}/role")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<UserProfileResponse>> updateRole(
            @PathVariable Long id,
            @RequestParam String role) {
        return ResponseEntity.ok(ApiResponse.success("Role updated", userService.updateUserRole(id, role)));
    }

    @PutMapping("/api/admin/users/{id}/toggle-status")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<UserProfileResponse>> toggleStatus(@PathVariable Long id) {
        return ResponseEntity.ok(ApiResponse.success("User status updated", userService.toggleUserStatus(id)));
    }

    @DeleteMapping("/api/admin/users/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<Void>> deleteUser(@PathVariable Long id) {
        userService.deleteUser(id);
        return ResponseEntity.ok(ApiResponse.success("User deleted", null));
    }

    @GetMapping("/api/admin/users/{id}/bookings")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<List<BookingDto>>> getUserBookings(@PathVariable Long id) {
        return ResponseEntity.ok(ApiResponse.success("User bookings", userService.getUserBookings(id)));
    }
}

