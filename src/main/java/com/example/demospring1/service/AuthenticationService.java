package com.example.demospring1.service;

import com.example.demospring1.exception.ResourceNotFoundException;
import com.example.demospring1.persistence.entity.Token;
import com.example.demospring1.persistence.entity.User;
import com.example.demospring1.persistence.repository.TokenRepository;
import com.example.demospring1.security.CustomUserDetailsService;
import com.example.demospring1.security.JwtUtil;
import com.example.demospring1.security.dto.AuthenticationResponse;
import com.example.demospring1.security.dto.LoginRequest;
import com.example.demospring1.security.dto.RegistrationRequest;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.util.List;

import static com.example.demospring1.security.JwtUtil.AUTH_TYPE;
import static org.springframework.http.HttpHeaders.AUTHORIZATION;

@Service
@RequiredArgsConstructor
public class AuthenticationService {

    private final AuthenticationManager authenticationManager;
    private final JwtUtil jwtUtil;
    private final UserService userService;
    private final TokenRepository tokenRepository;
    private final CustomUserDetailsService customUserDetailsService;

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
        saveToken(loginRequest.getUsername(), accessToken);
        saveToken(loginRequest.getUsername(), refreshToken);
        return AuthenticationResponse.builder()
                .withUsername(userDetails.getUsername())
                .withAccessToken(accessToken)
                .withRefreshToken(refreshToken)
                .build();
    }

    private void saveToken(String username, String token) {
        Token tokenEntity = new Token();
        User user = userService.getUserByUsername(username);
        tokenEntity.setUser(user);
        tokenEntity.setToken(token);
        tokenEntity.setExpired(false);
        tokenEntity.setRevoked(false);
        tokenEntity.setJti(jwtUtil.getJti(token));
        tokenRepository.save(tokenEntity);
    }

    private void revokeAllTokens(UserDetails user) {
        List<Token> validUserTokens = tokenRepository.findAllValidTokensByUser(user.getUsername());
        if (validUserTokens.isEmpty())
            return;
        validUserTokens.forEach(token -> {
            token.setExpired(true);
            token.setRevoked(true);
        });
        tokenRepository.saveAll(validUserTokens);
    }

    private void revokeToken(String token) {
        String jti = jwtUtil.getJti(token);
        Token validToken = tokenRepository.findByJti(jti);
        validToken.setExpired(true);
        validToken.setRevoked(true);
    }

    public AuthenticationResponse refreshToken(
            HttpServletRequest request,
            HttpServletResponse response
    ) {
        final String authHeader = request.getHeader(AUTHORIZATION);
        final String refreshToken;
        if (authHeader == null ||!authHeader.startsWith(AUTH_TYPE)) {
            throw new ResourceNotFoundException("Refresh token not found");
        }
        refreshToken = authHeader.substring(AUTH_TYPE.length()).trim();
        Token refresh = tokenRepository.findByJti(jwtUtil.getJti(refreshToken));
        if (refresh == null || refresh.isExpired() || refresh.isRevoked()) {
            throw new RuntimeException("Refresh token is invalid");
        }
        final String username = jwtUtil.getUsername(refreshToken);

        if (username != null) {
            UserDetails userDetails = customUserDetailsService.loadUserByUsername(username);
            if (jwtUtil.isVerified(refreshToken) && username.equals(userDetails.getUsername())) {
                String accessToken = jwtUtil.generateAccessToken(userDetails);
                String newRefreshToken = jwtUtil.generateRefreshToken(userDetails);
                revokeToken(refreshToken);
                saveToken(username, accessToken);
                saveToken(username, newRefreshToken);
                return AuthenticationResponse.builder()
                        .withUsername(username)
                        .withAccessToken(accessToken)
                        .withRefreshToken(newRefreshToken)
                        .build();
            }
        }

        throw new ResourceNotFoundException("User not found");
    }
}
