package com.extractor.email_extractor.service;


import com.extractor.email_extractor.entity.User;
import com.extractor.email_extractor.exception.UserAlreadyExistsException;
import com.extractor.email_extractor.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder; // Inject the password encoder


    @Override
    public User createUser(User user) {
        // Check if the username or email already exists
        if (userRepository.existsByUsername(user.getUsername())) {
            throw new UserAlreadyExistsException("This username already exists, please try a different username or login.");
        }
        if (userRepository.existsByUserEmail(user.getUserEmail())) {
            throw new UserAlreadyExistsException("This email address is already registered, please login.");
        }

        // Encode the password before saving
        user.setPassword(passwordEncoder.encode(user.getPassword()));

        // Save the user if validation passes
        return userRepository.save(user);
    }

    @Override
    public Optional<User> getUserById(Integer userId) {
        return userRepository.findById(userId);
    }

    @Override
    public Optional<User> getUserByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    @Override
    public boolean existsByUsername(String username) {
        return userRepository.existsByUsername(username);
    }

    @Override
    public boolean existsByEmail(String email) {
        return userRepository.existsByUserEmail(email);
    }

    @Override
    public void deleteUser(Integer userId) {
        userRepository.deleteById(userId);
    }

    // UserServiceImpl.java
    @Override
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    @Override
    public User updateUser(Integer userId, User updatedUser) {
        return userRepository.findById(userId)
                .map(existingUser -> {
                    // Perform validation for username and email before proceeding
                    if (!existingUser.getUsername().equals(updatedUser.getUsername()) && userRepository.existsByUsername(updatedUser.getUsername())) {
                        throw new UserAlreadyExistsException("This username already exists.");
                    }
                    if (!existingUser.getUserEmail().equals(updatedUser.getUserEmail()) && userRepository.existsByUserEmail(updatedUser.getUserEmail())) {
                        throw new UserAlreadyExistsException("This email already exists.");
                    }

                    // Update mutable fields
                    existingUser.setUsername(updatedUser.getUsername());
                    existingUser.setUserEmail(updatedUser.getUserEmail());

                    // Conditionally encode and update the password
                    // Only encode if the new password is not null/empty and is different from the old one
                    String newPassword = updatedUser.getPassword();
                    if (newPassword != null && !newPassword.isEmpty()) {
                        // It is essential to check if the password has actually changed before encoding
                        // To avoid re-encoding an already encoded password
                        // The password from the updatedUser object will be plain text,
                        // so we only encode if a new password is provided.
                        existingUser.setPassword(passwordEncoder.encode(newPassword));
                    }

                    // Save the updated user
                    return userRepository.save(existingUser);
                })
                .orElseThrow(() -> new RuntimeException("User not found with ID: " + userId));
    }
}
