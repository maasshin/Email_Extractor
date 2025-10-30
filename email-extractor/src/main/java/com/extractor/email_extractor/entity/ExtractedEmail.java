package com.extractor.email_extractor.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Entity
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
@Table(name = "extracted_emails") // Maps to the "extracted_emails" table
public class ExtractedEmail {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // Auto-increment primary key
    private int id;

    @Column(name = "email_address", nullable = false, length = 255) // The extracted email address
    private String emailAddress;

    @Column(name = "date_extracted", nullable = false, columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
    private LocalDateTime dateExtracted;

    // Many-to-One relationship: ExtractedEmail belongs to an EmailAccount
    @ManyToOne(fetch = FetchType.LAZY) // Lazy loading for performance
    @JoinColumn(name = "source_email_account_id", nullable = false) // Foreign key to the "email_accounts" table
    private EmailAccount sourceEmailAccount;

    // Default constructor
    public ExtractedEmail() {
        this.dateExtracted = LocalDateTime.now(); // Automatically set the extraction date
    }

    // Parameterized constructor (for custom usage)
    public ExtractedEmail(String emailAddress, EmailAccount sourceEmailAccount) {
        this.emailAddress = emailAddress;
        this.dateExtracted = LocalDateTime.now(); // Automatically set the extraction date
        this.sourceEmailAccount = sourceEmailAccount;
    }
}
