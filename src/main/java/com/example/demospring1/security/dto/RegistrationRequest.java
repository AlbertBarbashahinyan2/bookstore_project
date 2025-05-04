package com.example.demospring1.security.dto;

import com.example.demospring1.service.dto.UserRegistrationDto;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RegistrationRequest {
    @NotBlank
    private String firstName;

    @NotBlank
    private String lastName;

    @NotBlank
    @Email
    private String email;

    @NotBlank
    private String username;

    @NotBlank
    @Size(min = 8, message = "Password must be at least 8 characters long")
    private String password;

    public UserRegistrationDto toUserRegistrationDto() {
        return new UserRegistrationDto(
                this.firstName,
                this.lastName,
                this.email,
                this.username,
                this.password
        );
    }

    public LoginRequest toLoginRequest() {
        return new LoginRequest(
                this.username,
                this.password
        );
    }
}
