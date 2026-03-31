package com.moviebooking.repository;

import com.moviebooking.model.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@DisplayName("UserRepository Tests")
class UserRepositoryTest {

    @Autowired
    private UserRepository userRepository;

    private User user1, user2;

    @BeforeEach
    void setUp() {
        user1 = userRepository.save(User.builder()
                .name("John Doe").email("john@test.com")
                .password("hashed1").role(User.Role.USER).active(true).build());

        user2 = userRepository.save(User.builder()
                .name("Admin User").email("admin@test.com")
                .password("hashed2").role(User.Role.ADMIN).active(true).build());
    }

    @Test
    @DisplayName("findByEmail - found existing email")
    void findByEmail_existingEmail_returnsUser() {
        Optional<User> found = userRepository.findByEmail("john@test.com");
        assertThat(found).isPresent();
        assertThat(found.get().getName()).isEqualTo("John Doe");
    }

    @Test
    @DisplayName("findByEmail - unknown email returns empty")
    void findByEmail_unknownEmail_returnsEmpty() {
        Optional<User> found = userRepository.findByEmail("nobody@test.com");
        assertThat(found).isEmpty();
    }

    @Test
    @DisplayName("existsByEmail - returns true for existing email")
    void existsByEmail_existingEmail_returnsTrue() {
        assertThat(userRepository.existsByEmail("john@test.com")).isTrue();
    }

    @Test
    @DisplayName("existsByEmail - returns false for new email")
    void existsByEmail_newEmail_returnsFalse() {
        assertThat(userRepository.existsByEmail("new@test.com")).isFalse();
    }

    @Test
    @DisplayName("findByActiveTrue - returns only active users")
    void findByActiveTrue_returnsOnlyActiveUsers() {
        // Deactivate user1
        user1.setActive(false);
        userRepository.save(user1);

        List<User> activeUsers = userRepository.findByActiveTrue();
        assertThat(activeUsers).hasSize(1);
        assertThat(activeUsers.get(0).getEmail()).isEqualTo("admin@test.com");
    }

    @Test
    @DisplayName("findByRole - returns users with matching role")
    void findByRole_adminRole_returnsAdminUsers() {
        List<User> admins = userRepository.findByRole(User.Role.ADMIN);
        assertThat(admins).hasSize(1);
        assertThat(admins.get(0).getEmail()).isEqualTo("admin@test.com");
    }

    @Test
    @DisplayName("findByEmailAndActiveTrue - only returns active user")
    void findByEmailAndActiveTrue_inactiveUser_returnsEmpty() {
        user1.setActive(false);
        userRepository.save(user1);

        Optional<User> result = userRepository.findByEmailAndActiveTrue("john@test.com");
        assertThat(result).isEmpty();
    }

    @Test
    @DisplayName("save - persists all user fields")
    void save_persistsAllFields() {
        User newUser = userRepository.save(User.builder()
                .name("New User").email("new@test.com").password("hashed3")
                .phone("9876543210").role(User.Role.USER).active(true).build());

        User found = userRepository.findById(newUser.getId()).orElseThrow();
        assertThat(found.getPhone()).isEqualTo("9876543210");
        assertThat(found.isActive()).isTrue();
        assertThat(found.getCreatedAt()).isNotNull();
    }
}
