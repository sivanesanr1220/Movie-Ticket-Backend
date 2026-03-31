package com.moviebooking.repository;

import com.moviebooking.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

    // Find user by email (used for login + JWT)
    Optional<User> findByEmail(String email);

    // Check if email already exists (used for register)
    boolean existsByEmail(String email);

    // Get all active users only
    List<User> findByActiveTrue();

    // Get all users by role (e.g. all ADMINs)
    List<User> findByRole(User.Role role);

    // Find active user by email (login check with active status)
    Optional<User> findByEmailAndActiveTrue(String email);
}
