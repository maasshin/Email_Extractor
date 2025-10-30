package com.extractor.email_extractor.engine;

import com.extractor.email_extractor.dto.ExtractionStatusResponse;
import com.extractor.email_extractor.entity.EmailAccount;
import com.extractor.email_extractor.entity.ExtractedEmail;
import com.extractor.email_extractor.service.EmailAccountService;
import com.extractor.email_extractor.service.ExtractedEmailService;
import jakarta.mail.*;
import jakarta.mail.internet.InternetAddress;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class EmailExtractorEngine {

    private final EmailAccountService emailAccountService;
    private final ExtractedEmailService extractedEmailService;

    // Map to track detailed extraction status for each email account
    private final ConcurrentHashMap<Integer, ExtractionStatusResponse> extractionStatusMap = new ConcurrentHashMap<>();

    public EmailExtractorEngine(EmailAccountService emailAccountService, ExtractedEmailService extractedEmailService) {
        this.emailAccountService = emailAccountService;
        this.extractedEmailService = extractedEmailService;
    }

    @Async // Executes this method asynchronously
    public void extractEmails(EmailAccount emailAccount) {
        ExtractionStatusResponse statusResponse = new ExtractionStatusResponse();
        statusResponse.setStatus("Extraction in progress...");
        statusResponse.setLogMessages(new ArrayList<>());
        statusResponse.setEmailsExtracted(0);
        statusResponse.setTotalFolders(0);
        statusResponse.setProcessedFolders(0);

        extractionStatusMap.put(emailAccount.getId(), statusResponse); // Initialize status

        try {
            // Step 1: Log in to the email account
            Store store = loginToEmail(emailAccount);

            // Step 2: Start folder traversal and email extraction
            try (Store finalStore = store) {
                Folder[] folders = finalStore.getDefaultFolder().list(); // Get all folders
                statusResponse.setTotalFolders(folders.length); // Set the total number of folders

                for (Folder folder : folders) {
                    if ((folder.getType() & Folder.HOLDS_MESSAGES) != 0) {
                        logMessage(statusResponse, "Processing folder: " + folder.getName());
                        extractEmailsFromFolder(folder, emailAccount, statusResponse);
                        statusResponse.setProcessedFolders(statusResponse.getProcessedFolders() + 1); // Update processed folders
                    }
                }
            }

            // Step 3: Mark extraction as complete
            statusResponse.setStatus("Extraction complete!");
            logMessage(statusResponse, "Extraction completed for email: " + emailAccount.getEmailAddress());

        } catch (Exception e) {
            // Log failure details and update status
            statusResponse.setStatus("Extraction failed.");
            logMessage(statusResponse, "Failed to extract emails for: " + emailAccount.getEmailAddress());
            logMessage(statusResponse, "Error: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Fetch the status of the email extraction for a given account.
     */
    public ExtractionStatusResponse getExtractionStatus(Integer accountId) {
        return extractionStatusMap.getOrDefault(accountId, new ExtractionStatusResponse());
    }

    /**
     * Fetch extracted emails for a given email account.
     */
    public List<String> getExtractedEmails(Integer emailAccountId) {
        // Use the ExtractedEmailService to fetch emails from the database
        List<ExtractedEmail> extractedEmails = extractedEmailService.getExtractedEmailsByEmailAccountId(emailAccountId);

        // Convert the list of ExtractedEmail entities to a list of email strings
        List<String> emailAddresses = new ArrayList<>();
        for (ExtractedEmail extractedEmail : extractedEmails) {
            emailAddresses.add(extractedEmail.getEmailAddress());
        }

        return emailAddresses;
    }

    private Store loginToEmail(EmailAccount emailAccount) throws MessagingException {
        int maxRetries = 3; // Number of retries
        int attempts = 0;

        while (attempts < maxRetries) {
            try {
                // Set up email server properties
                Properties properties = new Properties();
                properties.put("mail.store.protocol", "imaps");
                properties.put("mail.imaps.host", emailAccount.getSmtpServer());
                properties.put("mail.imaps.port", emailAccount.getPort());
                properties.put("mail.imaps.ssl.enable", "true");
                properties.put("mail.imaps.ssl.trust", emailAccount.getSmtpServer());
                properties.put("mail.imaps.ssl.checkserveridentity", "false");

                // Create a session and connect to the email server
                Session session = Session.getInstance(properties);
                session.setDebug(false); // Set to true for debugging if needed
                Store store = session.getStore("imaps");
                store.connect(emailAccount.getEmailAddress(), emailAccount.getPasswordEncrypted());

                return store;

            } catch (MessagingException e) {
                attempts++;
                if (attempts >= maxRetries) {
                    throw new MessagingException("Failed to login after " + maxRetries + " attempts", e);
                }
                try {
                    Thread.sleep(2000); // Wait before retrying
                } catch (InterruptedException interruptedException) {
                    Thread.currentThread().interrupt();
                    throw new MessagingException("Login retry interrupted", interruptedException);
                }
            }
        }

        throw new MessagingException("Unexpected error: Login retry loop exited incorrectly");
    }

    private void extractEmailsFromFolder(Folder folder, EmailAccount emailAccount, ExtractionStatusResponse statusResponse) throws MessagingException {
        try (Folder finalFolder = folder) {
            finalFolder.open(Folder.READ_ONLY);

            Message[] messages = finalFolder.getMessages();
            logMessage(statusResponse, "Found " + messages.length + " emails in folder: " + finalFolder.getName());

            int checkpoint = 0;

            for (Message message : messages) {
                try {
                    Address[] fromAddresses = message.getFrom();
                    if (fromAddresses != null && fromAddresses.length > 0) {
                        String email = ((InternetAddress) fromAddresses[0]).getAddress();

                        ExtractedEmail extractedEmail = new ExtractedEmail();
                        extractedEmail.setEmailAddress(email);
                        extractedEmail.setSourceEmailAccount(emailAccount);
                        extractedEmailService.createExtractedEmail(extractedEmail);

                        statusResponse.setEmailsExtracted(statusResponse.getEmailsExtracted() + 1); // Update email count

                        checkpoint++;
                        if (checkpoint % 100 == 0) {
                            logMessage(statusResponse, "Checkpoint reached: " + checkpoint + " emails extracted.");
                        }
                    }
                } catch (Exception e) {
                    logMessage(statusResponse, "Failed to process an email in folder: " + finalFolder.getName());
                    e.printStackTrace();
                }
            }
        }
    }

    private void logMessage(ExtractionStatusResponse statusResponse, String message) {
        statusResponse.getLogMessages().add(message); // Add the log message
        System.out.println(message); // Optional: Print to console for debugging
    }
}