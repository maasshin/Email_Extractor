package com.extractor.email_extractor.service;

import com.extractor.email_extractor.entity.User;

import java.util.List;
import java.util.Optional;

public interface UserService {

    User createUser(User user);

    Optional<User> getUserById(Integer userId);

    Optional<User> getUserByUsername(String username);

    boolean existsByUsername(String username);

    boolean existsByEmail(String email);

    void deleteUser(Integer userId);

    // UserService.java
    List<User> getAllUsers();

    // update user
    User updateUser(Integer userId, User updatedUser);
}
