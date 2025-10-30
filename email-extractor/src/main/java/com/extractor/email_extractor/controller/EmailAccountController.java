package com.extractor.email_extractor.controller;

import com.extractor.email_extractor.dto.ExtractionStatusResponse;
import com.extractor.email_extractor.engine.EmailExtractorEngine;
import com.extractor.email_extractor.entity.EmailAccount;
import com.extractor.email_extractor.service.EmailAccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.ByteArrayInputStream;
import java.util.List;

@RestController
@RequestMapping("/email-accounts") // Base URL for email account-related endpoints
public class EmailAccountController {

    private final EmailAccountService emailAccountService;
    private final EmailExtractorEngine emailExtractorEngine;

    @Autowired
    public EmailAccountController(EmailAccountService emailAccountService, EmailExtractorEngine emailExtractorEngine) {
        this.emailAccountService = emailAccountService;
        this.emailExtractorEngine = emailExtractorEngine;
    }

    // Create a new email account
    @PostMapping
    public ResponseEntity<EmailAccount> createEmailAccount(@RequestBody EmailAccount emailAccount) {
        EmailAccount createdEmailAccount = emailAccountService.createEmailAccount(emailAccount);
        return ResponseEntity.ok(createdEmailAccount);
    }

    // Get email accounts by user ID
    @GetMapping("/user/{userId}")
    public ResponseEntity<List<EmailAccount>> getEmailAccountsByUserId(@PathVariable Integer userId) {
        List<EmailAccount> emailAccounts = emailAccountService.getEmailAccountsByUserId(userId);
        return ResponseEntity.ok(emailAccounts);
    }

    // Get an email account by ID
    @GetMapping("/{id}")
    public ResponseEntity<EmailAccount> getEmailAccountById(@PathVariable Integer id) {
        return emailAccountService.getEmailAccountById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // Update an existing email account by ID
    @PutMapping("/{id}")
    public ResponseEntity<EmailAccount> updateEmailAccount(@PathVariable Integer id, @RequestBody EmailAccount updatedEmailAccount) {
        try {
            EmailAccount result = emailAccountService.updateEmailAccount(id, updatedEmailAccount);
            return ResponseEntity.ok(result);
        } catch (RuntimeException e) {
            // Handle the case where the email account with the given ID is not found
            return ResponseEntity.notFound().build();
        }
    }

    // Delete an email account by ID
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteEmailAccount(@PathVariable Integer id) {
        emailAccountService.deleteEmailAccount(id);
        return ResponseEntity.noContent().build();
    }

    // Get all email accounts
    @GetMapping
    public ResponseEntity<List<EmailAccount>> findAllEmailAccounts() {
        List<EmailAccount> emailAccounts = emailAccountService.getAllEmailAccounts();
        return ResponseEntity.ok(emailAccounts);
    }

    // Trigger email extraction for a specific email account
    @PostMapping("/{id}/extract")
    public ResponseEntity<String> extractEmails(@PathVariable Integer id) {
        // Fetch the email account by ID
        EmailAccount emailAccount = emailAccountService.getEmailAccountById(id)
                .orElseThrow(() -> new RuntimeException("Email account not found"));

        // Trigger the email extraction process asynchronously
        emailExtractorEngine.extractEmails(emailAccount);

        // Return an immediate response
        return ResponseEntity.ok("Email extraction started for account: " + emailAccount.getEmailAddress());
    }

    /**
     * Endpoint to retrieve the list of extracted emails as JSON.
     * The frontend can then process this data to generate CSV or TXT.
     * This endpoint does not replace the existing export function.
     * @param id The ID of the email account.
     * @return A list of extracted emails in JSON format.
     */
    @GetMapping("/{id}/extracted-emails")
    public ResponseEntity<List<String>> getExtractedEmails(@PathVariable Integer id) {
        List<String> extractedEmails = emailExtractorEngine.getExtractedEmails(id);
        return ResponseEntity.ok(extractedEmails);
    }

    // endpoint to export extracted emails for a specific account as a CSV file
    @PostMapping("/{id}/export")
    public ResponseEntity<InputStreamResource> exportEmails(@PathVariable Integer id) {
        // Fetch extracted emails for the given account
        List<String> extractedEmails = emailExtractorEngine.getExtractedEmails(id);

        // Convert the emails list to CSV format
        StringBuilder csvBuilder = new StringBuilder("Email\n");
        extractedEmails.forEach(email -> csvBuilder.append(email).append("\n"));

        // Create a ByteArrayInputStream for the CSV data
        ByteArrayInputStream csvStream = new ByteArrayInputStream(csvBuilder.toString().getBytes());

        // Return the file as a downloadable resource
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=emails.csv")
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .body(new InputStreamResource(csvStream));
    }

    // an endpoint to provide the current extraction status.
    @GetMapping("/{id}/status")
    public ResponseEntity<ExtractionStatusResponse> getExtractionStatus(@PathVariable Integer id) {
        ExtractionStatusResponse status = emailExtractorEngine.getExtractionStatus(id);
        return ResponseEntity.ok(status);
    }
}