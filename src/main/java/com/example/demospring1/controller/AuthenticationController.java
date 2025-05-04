package com.example.demospring1.controller;

import com.example.demospring1.security.dto.AuthenticationResponse;
import com.example.demospring1.security.dto.LoginRequest;
import com.example.demospring1.security.dto.RegistrationRequest;
import com.example.demospring1.service.AuthenticationService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthenticationController {

    private final AuthenticationService authenticationService;

    @PostMapping("/login")
    public ResponseEntity<AuthenticationResponse> login(@Valid @RequestBody LoginRequest loginRequestDto) {
        return ResponseEntity.ok(authenticationService.authenticate(loginRequestDto));
    }

    @PostMapping("/register")
    public ResponseEntity<AuthenticationResponse> register(@Valid @RequestBody RegistrationRequest registrationRequestDto) {
        return ResponseEntity.ok(authenticationService.register(registrationRequestDto));
    }

    @PostMapping("/refresh")
    public ResponseEntity<AuthenticationResponse> refreshToken(HttpServletRequest request, HttpServletResponse response) throws IOException {
        AuthenticationResponse authResponse = authenticationService.refreshToken(request, response);
        if (authResponse == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(
                    AuthenticationResponse.builder()
                            .withAccessToken(null)
                            .withRefreshToken(null)
                            .withUsername(null)
                            .build()
            );
        }
        return ResponseEntity.ok(authResponse);
    }

}
