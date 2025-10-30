package com.extractor.email_extractor.dto;

import lombok.Data;

@Data
public class LoginRequest {
    private String username;
    private String password;
}
