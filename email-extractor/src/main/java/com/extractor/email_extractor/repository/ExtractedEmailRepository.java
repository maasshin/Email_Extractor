package com.extractor.email_extractor.repository;

import com.extractor.email_extractor.entity.ExtractedEmail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.util.List;

public interface ExtractedEmailRepository extends JpaRepository<ExtractedEmail, Integer> {

    // Find all extracted emails by the associated EmailAccount's id
    List<ExtractedEmail> findBySourceEmailAccountId(Integer sourceEmailAccountId);

    // Pagination: Find extracted emails by EmailAccount with pagination
    List<ExtractedEmail> findBySourceEmailAccountId(Integer sourceEmailAccountId, Pageable pageable);

    // Sorting: Find all extracted emails and sort by emailAddress
    List<ExtractedEmail> findAll(Sort sort);

    // Combined: Find extracted emails by EmailAccount with pagination and sorting
    //List<ExtractedEmail> findBySourceEmailAccountId(Integer sourceEmailAccountId, Pageable pageable, Sort sort);
}