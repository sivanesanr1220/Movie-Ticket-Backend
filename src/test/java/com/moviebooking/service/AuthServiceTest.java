package com.moviebooking.service;

import com.moviebooking.config.JwtUtils;
import com.moviebooking.dto.AuthDto.*;
import com.moviebooking.exception.BadRequestException;
import com.moviebooking.model.User;
import com.moviebooking.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.*;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("AuthService Tests")
class AuthServiceTest {

    @Mock private UserRepository userRepository;
    @Mock private PasswordEncoder passwordEncoder;
    @Mock private AuthenticationManager authenticationManager;
    @Mock private JwtUtils jwtUtils;

    @InjectMocks
    private AuthService authService;

    private User sampleUser;

    @BeforeEach
    void setUp() {
        sampleUser = User.builder()
                .id(1L).name("John Doe").email("john@test.com")
                .password("$2a$10$hashed").phone("9999999999")
                .role(User.Role.USER).active(true).build();
    }

    // ── REGISTER ──────────────────────────────────────────────

    @Test
    @DisplayName("register - should create user and return token")
    void register_newEmail_createsUserAndReturnsToken() {
        RegisterRequest request = new RegisterRequest();
        request.setName("John Doe");
        request.setEmail("john@test.com");
        request.setPassword("pass123");
        request.setPhone("9999999999");

        when(userRepository.existsByEmail("john@test.com")).thenReturn(false);
        when(passwordEncoder.encode("pass123")).thenReturn("$2a$10$hashed");
        when(userRepository.save(any(User.class))).thenReturn(sampleUser);

        Authentication mockAuth = mock(Authentication.class);
        when(authenticationManager.authenticate(any())).thenReturn(mockAuth);
        when(jwtUtils.generateToken(mockAuth)).thenReturn("mock.jwt.token");

        AuthResponse response = authService.register(request);

        assertThat(response.getToken()).isEqualTo("mock.jwt.token");
        assertThat(response.getEmail()).isEqualTo("john@test.com");
        assertThat(response.getRole()).isEqualTo("USER");
        verify(userRepository).save(any(User.class));
    }

    @Test
    @DisplayName("register - duplicate email throws BadRequest")
    void register_duplicateEmail_throwsBadRequest() {
        RegisterRequest request = new RegisterRequest();
        request.setEmail("john@test.com");

        when(userRepository.existsByEmail("john@test.com")).thenReturn(true);

        assertThatThrownBy(() -> authService.register(request))
                .isInstanceOf(BadRequestException.class)
                .hasMessage("Email is already registered");

        verify(userRepository, never()).save(any());
    }

    // ── LOGIN ─────────────────────────────────────────────────

    @Test
    @DisplayName("login - valid credentials returns token")
    void login_validCredentials_returnsToken() {
        LoginRequest request = new LoginRequest();
        request.setEmail("john@test.com");
        request.setPassword("pass123");

        Authentication mockAuth = mock(Authentication.class);
        when(authenticationManager.authenticate(any())).thenReturn(mockAuth);
        when(jwtUtils.generateToken(mockAuth)).thenReturn("mock.jwt.token");
        when(userRepository.findByEmail("john@test.com")).thenReturn(Optional.of(sampleUser));

        AuthResponse response = authService.login(request);

        assertThat(response.getToken()).isEqualTo("mock.jwt.token");
        assertThat(response.getName()).isEqualTo("John Doe");
    }

    @Test
    @DisplayName("login - deactivated account throws BadRequest")
    void login_deactivatedAccount_throwsBadRequest() {
        sampleUser.setActive(false);

        LoginRequest request = new LoginRequest();
        request.setEmail("john@test.com");
        request.setPassword("pass123");

        Authentication mockAuth = mock(Authentication.class);
        when(authenticationManager.authenticate(any())).thenReturn(mockAuth);
        when(jwtUtils.generateToken(mockAuth)).thenReturn("token");
        when(userRepository.findByEmail("john@test.com")).thenReturn(Optional.of(sampleUser));

        assertThatThrownBy(() -> authService.login(request))
                .isInstanceOf(BadRequestException.class)
                .hasMessageContaining("deactivated");
    }

    @Test
    @DisplayName("login - wrong password throws AuthenticationException")
    void login_wrongPassword_throwsAuthenticationException() {
        LoginRequest request = new LoginRequest();
        request.setEmail("john@test.com");
        request.setPassword("wrongpass");

        when(authenticationManager.authenticate(any()))
                .thenThrow(new BadCredentialsException("Bad credentials"));

        assertThatThrownBy(() -> authService.login(request))
                .isInstanceOf(BadCredentialsException.class);
    }
}
