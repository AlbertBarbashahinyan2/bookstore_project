package com.example.demospring1.service.dto;

import com.example.demospring1.persistence.entity.User;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserDto {

    private Long id;
    private String username;
    private String firstName;
    private String lastName;
    private String email;
    private Long createdAt;
    private Long updatedAt;

    public static UserDto toDto(User user) {
        final UserDto userDto = new UserDto();

        userDto.setId(user.getId());
        userDto.setUsername(user.getUsername());
        userDto.setFirstName(user.getFirstName());
        userDto.setLastName(user.getLastName());
        userDto.setEmail(user.getEmail());
        userDto.setCreatedAt(user.getCreatedAt().toEpochMilli());
        userDto.setUpdatedAt(user.getUpdatedAt().toEpochMilli());

        return userDto;
    }
}
