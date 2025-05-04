package com.example.demospring1.service;

import com.example.demospring1.persistence.entity.Token;
import com.example.demospring1.persistence.entity.User;
import com.example.demospring1.persistence.repository.TokenRepository;
import com.example.demospring1.persistence.repository.UserRepository;
import com.example.demospring1.security.JwtUtil;
import com.example.demospring1.security.dto.AuthenticationResponse;
import com.example.demospring1.security.dto.LoginRequest;
import com.example.demospring1.security.dto.RegistrationRequest;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.io.IOException;

import static com.example.demospring1.security.JwtUtil.AUTH_TYPE;
import static org.springframework.http.HttpHeaders.AUTHORIZATION;

@Service
@RequiredArgsConstructor
public class AuthenticationService {

    private final AuthenticationManager authenticationManager;
    private final JwtUtil jwtUtil;
    private final UserService userService;
    private final TokenRepository tokenRepository;
    private final UserRepository userRepository;

    public AuthenticationResponse register(RegistrationRequest request) {
        userService.createUser(request.toUserRegistrationDto());
        return authenticate(request.toLoginRequest());
    }

    public AuthenticationResponse authenticate(LoginRequest loginRequest) {
        final Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        loginRequest.getUsername(),
                        loginRequest.getPassword()
                )
        );

        final UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        final String accessToken = jwtUtil.generateAccessToken(userDetails);
        final String refreshToken = jwtUtil.generateRefreshToken(userDetails);

        return AuthenticationResponse.builder()
                .withUsername(userDetails.getUsername())
                .withAccessToken(accessToken)
                .withRefreshToken(refreshToken)
                .build();
    }

    private void saveToken(User user, String token) {
        Token tokenEntity = new Token();
        tokenEntity.setUser(user);
        tokenEntity.setToken(token);
        tokenEntity.setExpired(false);
        tokenEntity.setRevoked(false);
        tokenRepository.save(tokenEntity);
    }

    private void revokeAllTokens(User user) {
        var validUserTokens = tokenRepository.findAllValidTokensByUser(user.getId());
        if (validUserTokens.isEmpty())
            return;
        validUserTokens.forEach(token -> {
            token.setExpired(true);
            token.setRevoked(true);
        });
        tokenRepository.saveAll(validUserTokens);
    }

    public AuthenticationResponse refreshToken(
            HttpServletRequest request,
            HttpServletResponse response
    ) {
        final String authHeader = request.getHeader(AUTHORIZATION);
        final String refreshToken;
        if (authHeader == null ||!authHeader.startsWith(AUTH_TYPE)) {
            System.out.println("Refresh token is missing");
            return null;
        }
        refreshToken = authHeader.substring(AUTH_TYPE.length()).trim();
        final String username = jwtUtil.getUsername(refreshToken);

        if (username != null) {
            User user = userRepository.findByUsername(username)
                    .orElseThrow();
            if (jwtUtil.isVerified(refreshToken) && username.equals(user.getUsername())) {
                String accessToken = jwtUtil.generateAccessToken(user);
                revokeAllTokens(user);
                saveToken(user, accessToken);
                return AuthenticationResponse.builder()
                        .withUsername(username)
                        .withAccessToken(accessToken)
                        .withRefreshToken(refreshToken)
                        .build();
            }
        }

        return null;
    }
}
