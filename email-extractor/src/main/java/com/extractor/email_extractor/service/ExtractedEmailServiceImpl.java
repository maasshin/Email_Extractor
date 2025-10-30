package com.extractor.email_extractor.service;

import com.extractor.email_extractor.entity.ExtractedEmail;
import com.extractor.email_extractor.repository.ExtractedEmailRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ExtractedEmailServiceImpl implements ExtractedEmailService {

    @Autowired
    private ExtractedEmailRepository extractedEmailRepository;

    @Override
    public ExtractedEmail createExtractedEmail(ExtractedEmail extractedEmail) {
        return extractedEmailRepository.save(extractedEmail);
    }

    //private final ExtractedEmailRepository extractedEmailRepository;

    public ExtractedEmailServiceImpl(ExtractedEmailRepository extractedEmailRepository) {
        this.extractedEmailRepository = extractedEmailRepository;
    }

    @Override
    public List<ExtractedEmail> getExtractedEmailsByEmailAccountId(Integer emailAccountId) {
        return extractedEmailRepository.findBySourceEmailAccountId(emailAccountId);
    }

    @Override
    public void deleteExtractedEmail(Integer extractedEmailId) {
        extractedEmailRepository.deleteById(extractedEmailId);
    }

    // ExtractedEmailServiceImpl.java
    @Override
    public List<ExtractedEmail> getAllExtractedEmails() {
        return extractedEmailRepository.findAll();
    }
}
