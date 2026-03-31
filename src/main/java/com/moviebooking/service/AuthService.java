package com.moviebooking.service;

import com.moviebooking.config.JwtUtils;
import com.moviebooking.dto.AuthDto.*;
import com.moviebooking.exception.BadRequestException;
import com.moviebooking.model.User;
import com.moviebooking.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.*;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtUtils jwtUtils;

    // ── REGISTER ──────────────────────────────────────────────
    public AuthResponse register(RegisterRequest request) {
        // Check duplicate email
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new BadRequestException("Email is already registered");
        }
        // Save new user with hashed password
        User user = User.builder()
                .name(request.getName())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .phone(request.getPhone())
                .role(User.Role.USER)
                .active(true)
                .build();
        userRepository.save(user);

        // Auto-login: generate token right after register
        Authentication auth = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword()));
        String token = jwtUtils.generateToken(auth);

        return new AuthResponse(token, user.getId(), user.getName(),
                user.getEmail(), user.getPhone(), user.getRole().name());
    }

    // ── LOGIN ─────────────────────────────────────────────────
    public AuthResponse login(LoginRequest request) {
        // Validates credentials against DB — throws exception if wrong
        Authentication auth = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword()));

        String token = jwtUtils.generateToken(auth);
        User user = userRepository.findByEmail(request.getEmail()).orElseThrow();

        if (!user.isActive()) {
            throw new BadRequestException("Your account has been deactivated. Contact support.");
        }

        return new AuthResponse(token, user.getId(), user.getName(),
                user.getEmail(), user.getPhone(), user.getRole().name());
    }
}
