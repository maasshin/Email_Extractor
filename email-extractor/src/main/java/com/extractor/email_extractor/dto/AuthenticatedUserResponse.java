package com.extractor.email_extractor.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AuthenticatedUserResponse {
    private String jwt;
    private int id;
    private String username;
}

