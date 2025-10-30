package com.extractor.email_extractor.service;

import com.extractor.email_extractor.entity.ExtractedEmail;

import java.util.List;

public interface ExtractedEmailService {

    ExtractedEmail createExtractedEmail(ExtractedEmail extractedEmail);

    List<ExtractedEmail> getExtractedEmailsByEmailAccountId(Integer emailAccountId);

    void deleteExtractedEmail(Integer extractedEmailId);

    // ExtractedEmailService.java
    List<ExtractedEmail> getAllExtractedEmails();
}
