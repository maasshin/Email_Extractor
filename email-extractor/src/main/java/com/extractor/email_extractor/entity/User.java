package com.extractor.email_extractor.entity;


import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "users") // Maps the class to the "users" table
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // Auto-increment primary key
    private int id;

    @Column(unique = true, nullable = false, length = 255) // Unique, not null
    private String username;

    @Column(name = "user_email", unique = true, nullable = false, length = 255) // Custom column name
    private String userEmail;

    @Column(nullable = false, length = 255) // Not null
    private String password;

    @Column(name = "created_at", nullable = false, columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
    private LocalDateTime createdAt;

    // Default constructor
    public User() {
        this.createdAt = LocalDateTime.now(); // Set the creation timestamp
    }
}

