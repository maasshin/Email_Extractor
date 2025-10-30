package com.extractor.email_extractor.repository;

import com.extractor.email_extractor.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Integer> {

    // Custom query methods with meaningful names

    /**
     * Find a user by their username.
     * @param username the username
     * @return an Optional containing the user, or empty if not found
     */
    Optional<User> findByUsername(String username);

    /**
     * Find a user by their email address.
     * @param userEmail the user's email
     * @return an Optional containing the user, or empty if not found
     */
    Optional<User> findByUserEmail(String userEmail);

    /**
     * Check if a user exists by their username.
     * @param username the username
     * @return true if the user exists, otherwise false
     */
    boolean existsByUsername(String username);

    /**
     * Check if a user exists by their email address.
     * @param userEmail the email address
     * @return true if the user exists, otherwise false
     */
    boolean existsByUserEmail(String userEmail);
}
