package com.moviebooking.service;

import com.moviebooking.dto.BookingDto;
import com.moviebooking.dto.UserDto.*;
import com.moviebooking.exception.BadRequestException;
import com.moviebooking.exception.ResourceNotFoundException;
import com.moviebooking.model.User;
import com.moviebooking.repository.BookingRepository;
import com.moviebooking.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final BookingRepository bookingRepository;
    private final PasswordEncoder passwordEncoder;

    // ══════════════════════════════════════════════════════════
    //  USER SELF-SERVICE
    // ══════════════════════════════════════════════════════════

    // ── GET MY PROFILE ────────────────────────────────────────
    public UserProfileResponse getMyProfile(String email) {
        User user = findByEmail(email);
        return UserProfileResponse.from(user);
    }

    // ── UPDATE MY PROFILE (name, phone) ──────────────────────
    public UserProfileResponse updateMyProfile(String email, UpdateProfileRequest request) {
        User user = findByEmail(email);
        user.setName(request.getName());
        user.setPhone(request.getPhone());
        return UserProfileResponse.from(userRepository.save(user));
    }

    // ── CHANGE MY PASSWORD ────────────────────────────────────
    public void changePassword(String email, ChangePasswordRequest request) {
        User user = findByEmail(email);

        // Step 1: verify old password matches
        if (!passwordEncoder.matches(request.getOldPassword(), user.getPassword())) {
            throw new BadRequestException("Old password is incorrect");
        }

        // Step 2: make sure new password is different
        if (request.getOldPassword().equals(request.getNewPassword())) {
            throw new BadRequestException("New password must be different from old password");
        }

        // Step 3: save new hashed password
        user.setPassword(passwordEncoder.encode(request.getNewPassword()));
        userRepository.save(user);
    }

    // ── GET MY BOOKING HISTORY ────────────────────────────────
    public List<BookingDto> getMyBookingHistory(String email) {
        User user = findByEmail(email);
        return bookingRepository.findByUserId(user.getId())
                .stream()
                .map(BookingDto::from)
                .toList();
    }

    // ── DELETE MY ACCOUNT (soft delete) ──────────────────────
    public void deactivateMyAccount(String email) {
        User user = findByEmail(email);
        user.setActive(false);           // sets active = false in DB
        userRepository.save(user);       // does NOT actually delete the row
    }

    // ══════════════════════════════════════════════════════════
    //  ADMIN OPERATIONS
    // ══════════════════════════════════════════════════════════

    // ── GET ALL USERS (admin only) ────────────────────────────
    public List<UserProfileResponse> getAllUsers() {
        return userRepository.findAll()
                .stream()
                .map(UserProfileResponse::from)
                .toList();
    }

    // ── GET USER BY ID (admin only) ───────────────────────────
    public UserProfileResponse getUserById(Long id) {
        return UserProfileResponse.from(findById(id));
    }

    // ── CREATE USER (admin only) ─────────────────────────────
    public UserProfileResponse adminCreateUser(AdminCreateUserRequest request) {
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new BadRequestException("Email is already registered");
        }
        User user = User.builder()
                .name(request.getName())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .phone(request.getPhone())
                .role(request.getRole() != null ? request.getRole() : User.Role.USER)
                .active(true)
                .build();
        return UserProfileResponse.from(userRepository.save(user));
    }

    // ── UPDATE USER ROLE (admin only) ─────────────────────────
    public UserProfileResponse updateUserRole(Long id, String role) {
        User user = findById(id);
        try {
            user.setRole(User.Role.valueOf(role.toUpperCase()));
        } catch (IllegalArgumentException e) {
            throw new BadRequestException("Invalid role. Use USER or ADMIN");
        }
        return UserProfileResponse.from(userRepository.save(user));
    }

    // ── ACTIVATE / DEACTIVATE USER (admin only) ───────────────
    public UserProfileResponse toggleUserStatus(Long id) {
        User user = findById(id);
        user.setActive(!user.isActive());  // flip the status
        return UserProfileResponse.from(userRepository.save(user));
    }

    // ── DELETE USER PERMANENTLY (admin only) ──────────────────
    public void deleteUser(Long id) {
        User user = findById(id);
        userRepository.delete(user);
    }

    // ── GET BOOKINGS OF ANY USER (admin only) ─────────────────
    public List<BookingDto> getUserBookings(Long userId) {
        findById(userId); // validate user exists
        return bookingRepository.findByUserId(userId)
                .stream()
                .map(BookingDto::from)
                .toList();
    }

    // ══════════════════════════════════════════════════════════
    //  PRIVATE HELPERS
    // ══════════════════════════════════════════════════════════

    private User findByEmail(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
    }

    private User findById(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + id));
    }
}
