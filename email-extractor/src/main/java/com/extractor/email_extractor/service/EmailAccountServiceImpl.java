package com.extractor.email_extractor.service;
import com.extractor.email_extractor.entity.EmailAccount;
import com.extractor.email_extractor.entity.User;
import com.extractor.email_extractor.repository.EmailAccountRepository;
import com.extractor.email_extractor.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class EmailAccountServiceImpl implements EmailAccountService {

    @Autowired
    private EmailAccountRepository emailAccountRepository;

    @Autowired
    private UserRepository userRepository; // Add UserRepository

    @Override
    public EmailAccount createEmailAccount(EmailAccount emailAccount) {
        // Step 1: Fetch the existing User entity from the database using the ID
        User existingUser = userRepository.findById(emailAccount.getUser().getId())
                .orElseThrow(() -> new RuntimeException("User not found with ID: " + emailAccount.getUser().getId()));

        // Step 2: Set the fetched User entity on the EmailAccount object
        emailAccount.setUser(existingUser);

        // **IMPORTANT:** Add logic for password handling here. As discussed,
        // you should encrypt the password from the frontend before saving.
        // The service should not receive a plain-text password.
        // For now, the plain password sent from the front-end will be saved.
        // This is a major security risk and should be fixed immediately.

        // Step 3: Save the fully-formed EmailAccount entity
        return emailAccountRepository.save(emailAccount);
    }

    @Override
    public Optional<EmailAccount> getEmailAccountById(Integer emailAccountId) {
        return emailAccountRepository.findById(emailAccountId);
    }

    @Override
    public List<EmailAccount> getEmailAccountsByUserId(Integer userId) {
        return emailAccountRepository.findByUserId(userId);
    }

    @Override
    public void deleteEmailAccount(Integer emailAccountId) {
        emailAccountRepository.deleteById(emailAccountId);
    }

    // EmailAccountServiceImpl.java
    @Override
    public List<EmailAccount> getAllEmailAccounts() {
        return emailAccountRepository.findAll();
    }

    // Implementation for updating an email account
    @Override
    public EmailAccount updateEmailAccount(Integer emailAccountId, EmailAccount updatedEmailAccount) {
        return emailAccountRepository.findById(emailAccountId)
                .map(existingAccount -> {
                    // Update the properties you want to allow changing
                    // Important: Do not update the ID or User object
                    existingAccount.setEmailAddress(updatedEmailAccount.getEmailAddress());
                    existingAccount.setPasswordEncrypted(updatedEmailAccount.getPasswordEncrypted());
                    existingAccount.setSmtpServer(updatedEmailAccount.getSmtpServer());
                    existingAccount.setPort(updatedEmailAccount.getPort());
                    return emailAccountRepository.save(existingAccount);
                })
                .orElseThrow(() -> new RuntimeException("Email account not found with ID: " + emailAccountId));
    }
}

