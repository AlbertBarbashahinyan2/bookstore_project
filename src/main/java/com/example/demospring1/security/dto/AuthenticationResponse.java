package com.example.demospring1.security.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder(setterPrefix = "with")
public class AuthenticationResponse {

    private String username;
    private String accessToken;
    private String refreshToken;
}
