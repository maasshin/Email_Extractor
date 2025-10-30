package com.extractor.email_extractor.service;


import com.extractor.email_extractor.entity.EmailAccount;

import java.util.List;
import java.util.Optional;

public interface EmailAccountService {

    EmailAccount createEmailAccount(EmailAccount emailAccount);


    Optional<EmailAccount> getEmailAccountById(Integer emailAccountId);

    List<EmailAccount> getEmailAccountsByUserId(Integer userId);

    void deleteEmailAccount(Integer emailAccountId);

    List<EmailAccount> getAllEmailAccounts();

    // New method for updating an email account
    EmailAccount updateEmailAccount(Integer emailAccountId, EmailAccount updatedEmailAccount);
}
