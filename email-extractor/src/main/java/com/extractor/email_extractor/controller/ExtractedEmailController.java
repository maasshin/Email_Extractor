package com.extractor.email_extractor.controller;

import com.extractor.email_extractor.entity.ExtractedEmail;
import com.extractor.email_extractor.service.ExtractedEmailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/extracted-emails") // Base URL for extracted email-related endpoints
public class ExtractedEmailController {

    @Autowired
    private ExtractedEmailService extractedEmailService;

    // Create a new extracted email
    @PostMapping
    public ResponseEntity<ExtractedEmail> createExtractedEmail(@RequestBody ExtractedEmail extractedEmail) {
        ExtractedEmail createdExtractedEmail = extractedEmailService.createExtractedEmail(extractedEmail);
        return ResponseEntity.ok(createdExtractedEmail);
    }

    // Get extracted emails by email account ID
    @GetMapping("/email-account/{emailAccountId}")
    public ResponseEntity<List<ExtractedEmail>> getExtractedEmailsByEmailAccountId(@PathVariable Integer emailAccountId) {
        List<ExtractedEmail> extractedEmails = extractedEmailService.getExtractedEmailsByEmailAccountId(emailAccountId);
        return ResponseEntity.ok(extractedEmails);
    }

    // Delete an extracted email by ID
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteExtractedEmail(@PathVariable Integer id) {
        extractedEmailService.deleteExtractedEmail(id);
        return ResponseEntity.noContent().build();
    }

    // Get all extracted emails
    @GetMapping
    public ResponseEntity<List<ExtractedEmail>> findAllExtractedEmails() {
        List<ExtractedEmail> extractedEmails = extractedEmailService.getAllExtractedEmails(); // Add this method in ExtractedEmailService
        return ResponseEntity.ok(extractedEmails);
    }
}