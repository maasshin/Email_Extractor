package com.extractor.email_extractor.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
@Table(name = "email_accounts") // Maps to the "email_accounts" table
public class EmailAccount {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // Auto-increment primary key
    private int id;

    @Column(name = "email_address", unique = true, nullable = false, length = 255) // Unique, not null
    private String emailAddress;

    @Column(name = "password_encrypted", nullable = false, length = 255) // Encrypted password
    private String passwordEncrypted;

    @Column(name = "smtp_server", length = 255) // SMTP server
    private String smtpServer;

    @Column(name = "port") // Port number
    private int port;

    // Many-to-One relationship: EmailAccount belongs to a User
    @ManyToOne(fetch = FetchType.LAZY) // Lazy loading for better performance
    @JoinColumn(name = "user_id", nullable = false) // Foreign key to the "users" table
    private User user;

    // Default constructor
    public EmailAccount() {
    }

    // Parameterized constructor (optional for testing or custom usage)
    public EmailAccount(String emailAddress, String passwordEncrypted, String smtpServer, int port, User user) {
        this.emailAddress = emailAddress;
        this.passwordEncrypted = passwordEncrypted;
        this.smtpServer = smtpServer;
        this.port = port;
        this.user = user;
    }
}