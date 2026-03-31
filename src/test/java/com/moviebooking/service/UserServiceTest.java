package com.moviebooking.service;

import com.moviebooking.dto.UserDto.*;
import com.moviebooking.exception.BadRequestException;
import com.moviebooking.exception.ResourceNotFoundException;
import com.moviebooking.model.User;
import com.moviebooking.repository.BookingRepository;
import com.moviebooking.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("UserService Tests")
class UserServiceTest {

    @Mock private UserRepository userRepository;
    @Mock private BookingRepository bookingRepository;
    @Mock private PasswordEncoder passwordEncoder;

    @InjectMocks
    private UserService userService;

    private User sampleUser;

    @BeforeEach
    void setUp() {
        sampleUser = User.builder()
                .id(1L)
                .name("John Doe")
                .email("john@test.com")
                .password("$2a$10$hashedpassword")
                .phone("9876543210")
                .role(User.Role.USER)
                .active(true)
                .build();
    }

    // ── GET PROFILE ───────────────────────────────────────────

    @Test
    @DisplayName("getMyProfile - should return profile for valid email")
    void getMyProfile_validEmail_returnsProfile() {
        when(userRepository.findByEmail("john@test.com")).thenReturn(Optional.of(sampleUser));

        UserProfileResponse response = userService.getMyProfile("john@test.com");

        assertThat(response.getName()).isEqualTo("John Doe");
        assertThat(response.getEmail()).isEqualTo("john@test.com");
        assertThat(response.getPhone()).isEqualTo("9876543210");
        assertThat(response.getRole()).isEqualTo("USER");
    }

    @Test
    @DisplayName("getMyProfile - should throw exception for unknown email")
    void getMyProfile_unknownEmail_throwsException() {
        when(userRepository.findByEmail("unknown@test.com")).thenReturn(Optional.empty());

        assertThatThrownBy(() -> userService.getMyProfile("unknown@test.com"))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessage("User not found");
    }

    // ── UPDATE PROFILE ────────────────────────────────────────

    @Test
    @DisplayName("updateMyProfile - should update name and phone")
    void updateMyProfile_validRequest_updatesAndReturns() {
        UpdateProfileRequest request = new UpdateProfileRequest();
        request.setName("John Updated");
        request.setPhone("1234567890");

        when(userRepository.findByEmail("john@test.com")).thenReturn(Optional.of(sampleUser));
        when(userRepository.save(any(User.class))).thenAnswer(inv -> inv.getArgument(0));

        UserProfileResponse response = userService.updateMyProfile("john@test.com", request);

        assertThat(response.getName()).isEqualTo("John Updated");
        assertThat(response.getPhone()).isEqualTo("1234567890");
        verify(userRepository).save(any(User.class));
    }

    // ── CHANGE PASSWORD ───────────────────────────────────────

    @Test
    @DisplayName("changePassword - should succeed with correct old password")
    void changePassword_correctOldPassword_changesPassword() {
        ChangePasswordRequest request = new ChangePasswordRequest();
        request.setOldPassword("oldpass");
        request.setNewPassword("newpass123");

        when(userRepository.findByEmail("john@test.com")).thenReturn(Optional.of(sampleUser));
        when(passwordEncoder.matches("oldpass", sampleUser.getPassword())).thenReturn(true);
        when(passwordEncoder.encode("newpass123")).thenReturn("$2a$10$newhashedpassword");

        userService.changePassword("john@test.com", request);

        verify(userRepository).save(any(User.class));
    }

    @Test
    @DisplayName("changePassword - should throw if old password is wrong")
    void changePassword_wrongOldPassword_throwsBadRequest() {
        ChangePasswordRequest request = new ChangePasswordRequest();
        request.setOldPassword("wrongpass");
        request.setNewPassword("newpass123");

        when(userRepository.findByEmail("john@test.com")).thenReturn(Optional.of(sampleUser));
        when(passwordEncoder.matches("wrongpass", sampleUser.getPassword())).thenReturn(false);

        assertThatThrownBy(() -> userService.changePassword("john@test.com", request))
                .isInstanceOf(BadRequestException.class)
                .hasMessage("Old password is incorrect");

        verify(userRepository, never()).save(any());
    }

    @Test
    @DisplayName("changePassword - should throw if new == old")
    void changePassword_sameAsOld_throwsBadRequest() {
        ChangePasswordRequest request = new ChangePasswordRequest();
        request.setOldPassword("samepass");
        request.setNewPassword("samepass");

        when(userRepository.findByEmail("john@test.com")).thenReturn(Optional.of(sampleUser));
        when(passwordEncoder.matches("samepass", sampleUser.getPassword())).thenReturn(true);

        assertThatThrownBy(() -> userService.changePassword("john@test.com", request))
                .isInstanceOf(BadRequestException.class)
                .hasMessage("New password must be different from old password");
    }

    // ── DEACTIVATE ACCOUNT ────────────────────────────────────

    @Test
    @DisplayName("deactivateMyAccount - should set active to false")
    void deactivateMyAccount_setsActiveFalse() {
        when(userRepository.findByEmail("john@test.com")).thenReturn(Optional.of(sampleUser));
        when(userRepository.save(any(User.class))).thenAnswer(inv -> inv.getArgument(0));

        userService.deactivateMyAccount("john@test.com");

        assertThat(sampleUser.isActive()).isFalse();
        verify(userRepository).save(sampleUser);
    }

    // ── ADMIN: GET ALL USERS ──────────────────────────────────

    @Test
    @DisplayName("getAllUsers - should return all users")
    void getAllUsers_returnsAllUsers() {
        User admin = User.builder().id(2L).name("Admin").email("admin@test.com")
                .role(User.Role.ADMIN).active(true).build();
        when(userRepository.findAll()).thenReturn(List.of(sampleUser, admin));

        List<UserProfileResponse> result = userService.getAllUsers();

        assertThat(result).hasSize(2);
        assertThat(result.get(0).getEmail()).isEqualTo("john@test.com");
        assertThat(result.get(1).getRole()).isEqualTo("ADMIN");
    }

    // ── ADMIN: UPDATE ROLE ────────────────────────────────────

    @Test
    @DisplayName("updateUserRole - should update role to ADMIN")
    void updateUserRole_validRole_updatesRole() {
        when(userRepository.findById(1L)).thenReturn(Optional.of(sampleUser));
        when(userRepository.save(any())).thenAnswer(inv -> inv.getArgument(0));

        UserProfileResponse response = userService.updateUserRole(1L, "ADMIN");

        assertThat(response.getRole()).isEqualTo("ADMIN");
    }

    @Test
    @DisplayName("updateUserRole - invalid role throws BadRequest")
    void updateUserRole_invalidRole_throwsBadRequest() {
        when(userRepository.findById(1L)).thenReturn(Optional.of(sampleUser));

        assertThatThrownBy(() -> userService.updateUserRole(1L, "SUPERUSER"))
                .isInstanceOf(BadRequestException.class)
                .hasMessageContaining("Invalid role");
    }

    // ── ADMIN: TOGGLE STATUS ──────────────────────────────────

    @Test
    @DisplayName("toggleUserStatus - active user becomes inactive")
    void toggleUserStatus_activeUser_becomesInactive() {
        sampleUser.setActive(true);
        when(userRepository.findById(1L)).thenReturn(Optional.of(sampleUser));
        when(userRepository.save(any())).thenAnswer(inv -> inv.getArgument(0));

        UserProfileResponse result = userService.toggleUserStatus(1L);

        assertThat(result.isActive()).isFalse();
    }

    // ── ADMIN: DELETE USER ────────────────────────────────────

    @Test
    @DisplayName("deleteUser - should call repository delete")
    void deleteUser_existingUser_deletesSuccessfully() {
        when(userRepository.findById(1L)).thenReturn(Optional.of(sampleUser));

        userService.deleteUser(1L);

        verify(userRepository).delete(sampleUser);
    }

    @Test
    @DisplayName("deleteUser - non-existing user throws ResourceNotFound")
    void deleteUser_notFound_throwsException() {
        when(userRepository.findById(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> userService.deleteUser(99L))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessageContaining("99");
    }
}
