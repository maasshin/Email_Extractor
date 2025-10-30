package com.extractor.email_extractor.controller;

import com.extractor.email_extractor.entity.User;
import com.extractor.email_extractor.exception.UserAlreadyExistsException;
import com.extractor.email_extractor.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/users") // Base URL for user-related endpoints
public class UserController {

    @Autowired
    private UserService userService;

    // Create a new user
    @PostMapping
    public ResponseEntity<?> createUser(@RequestBody User user ) {
        // Validate input
        if (user.getUsername() == null || user.getUsername().isEmpty()) {
            return ResponseEntity.badRequest().body("Username is required.");
        }
        if (user.getUserEmail() == null || user.getUserEmail().isEmpty()) {
            return ResponseEntity.badRequest().body("Email is required.");
        }
        if (user.getPassword() == null || user.getPassword().isEmpty()) {
            return ResponseEntity.badRequest().body("Password is required.");
        }
        // Check if the username or email already exists
        if (userService.existsByUsername(user.getUsername())) {
            throw new UserAlreadyExistsException("This username already exists, please try a different username or login.");
        }
        if (userService.existsByEmail(user.getUserEmail())) {
            throw new UserAlreadyExistsException("This email address is already registered, please login.");
        }

        // Create and return the user
        User createdUser = userService.createUser(user);
        return ResponseEntity.ok(createdUser);
    }

    // Get a user by ID
    @GetMapping("/{id}")
    public ResponseEntity<User> getUserById(@PathVariable Integer id) {
        Optional<User> user = userService.getUserById(id);

        if (user.isPresent()) {
            return ResponseEntity.ok(user.get());
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    // Update an existing user by ID
    @PutMapping("/{id}")
    public ResponseEntity<User> updateUser(@PathVariable Integer id, @RequestBody User updatedUser) {
        try {
            User result = userService.updateUser(id, updatedUser);
            return ResponseEntity.ok(result);
        } catch (RuntimeException e) {
            // Handle the case where the user with the given ID is not found
            return ResponseEntity.notFound().build();
        }
    }

    // Get all users
    @GetMapping
    public ResponseEntity<List<User>> findAllUsers() {
        List<User> users = userService.getAllUsers(); // Add this method in UserService
        return ResponseEntity.ok(users);
    }

    // Delete a user by ID
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable Integer id) {
        userService.deleteUser(id);
        return ResponseEntity.noContent().build();
    }
}
