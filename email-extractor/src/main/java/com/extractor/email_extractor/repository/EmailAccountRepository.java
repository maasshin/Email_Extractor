package com.extractor.email_extractor.repository;

import com.extractor.email_extractor.entity.EmailAccount;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.util.List;

public interface EmailAccountRepository extends JpaRepository<EmailAccount, Integer> {
    // Custom query methods with meaningful names
    /**
     * Find all email accounts by the associated user's id.
     * @param userId the user's id
     * @return a list of email accounts
     */
    List<EmailAccount> findByUserId(Integer userId);
    /**
     * Find an email account by its email address.
     * @param emailAddress the email address
     * @return the email account
     */
    EmailAccount findByEmailAddress(String emailAddress);
    /**
     * Check if an email account exists by its email address.
     * @param emailAddress the email address
     * @return true if the email account exists, otherwise false
     */
    boolean existsByEmailAddress(String emailAddress);
    // Pagination: Find email accounts by userId with pagination
    List<EmailAccount> findByUserId(Integer userId, Pageable pageable);
    // Sorting: Find all email accounts and sort by emailAddress
    List<EmailAccount> findAll(Sort sort);
    // Combined: Find email accounts by userId with both pagination and sorting
    //List<EmailAccount> findByUserId(Integer userId, Pageable pageable);

}
