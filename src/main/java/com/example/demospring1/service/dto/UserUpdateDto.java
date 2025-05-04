package com.example.demospring1.service.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserUpdateDto {

    @NotBlank
    private String firstName;

    @NotBlank
    private String lastName;
}
