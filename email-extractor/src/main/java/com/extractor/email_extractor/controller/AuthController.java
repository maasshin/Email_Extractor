package com.extractor.email_extractor.controller;

import com.extractor.email_extractor.dto.AuthenticatedUserResponse;
import com.extractor.email_extractor.dto.LoginRequest;
//import com.extractor.email_extractor.dto.SignupRequest;
import com.extractor.email_extractor.dto.LoginResponse;
import com.extractor.email_extractor.entity.User;
import com.extractor.email_extractor.jwt.JwtUtil;
//import com.extractor.email_extractor.security.JwtService;
import com.extractor.email_extractor.repository.UserRepository;
import com.extractor.email_extractor.service.CustomUserDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;

@RestController
public class AuthController {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private CustomUserDetailsService userDetailsService;

    @Autowired
    private UserRepository userRepository;

    @PostMapping("/login")
    public ResponseEntity<?> createAuthenticationToken(@RequestBody LoginRequest loginRequest) throws Exception {
        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword())
            );
        } catch (BadCredentialsException e) {
            throw new Exception("Incorrect username or password", e);
        }

        final UserDetails userDetails = userDetailsService.loadUserByUsername(loginRequest.getUsername());
        final String jwt = jwtUtil.generateToken(userDetails);

        // Find  User entity using the username from the UserDetails object
        User user = userRepository.findByUsername(userDetails.getUsername())
                .orElseThrow(() -> new RuntimeException("User not found after successful authentication."));

        return ResponseEntity.ok(new AuthenticatedUserResponse(jwt, user.getId(), user.getUsername()));
    }
    // Example of a protected endpoint
    @GetMapping("/me")
    public String getLoggedInUser() {
        return "Welcome to a protected endpoint!";
    }
}