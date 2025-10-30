package com.extractor.email_extractor.dto;

import lombok.Data;
import java.util.List;

@Data
public class ExtractionStatusResponse {
    private String status;
    private List<String> logMessages;
    private int emailsExtracted;
    private int totalFolders;
    private int processedFolders;
}

