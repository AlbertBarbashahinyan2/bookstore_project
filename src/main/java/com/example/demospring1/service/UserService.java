package com.example.demospring1.service;

import com.example.demospring1.exception.ResourceAlreadyUsedException;
import com.example.demospring1.exception.ResourceNotFoundException;
import com.example.demospring1.persistence.entity.Role;
import com.example.demospring1.persistence.entity.User;
import com.example.demospring1.persistence.repository.RoleRepository;
import com.example.demospring1.persistence.repository.UserRepository;
import com.example.demospring1.service.dto.UserDto;
import com.example.demospring1.service.dto.UserRegistrationDto;
import com.example.demospring1.service.dto.UserUpdateDto;
import com.example.demospring1.service.enums.RoleName;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/users")
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final RoleRepository roleRepository;

    @Transactional
    public UserDto createUser(UserRegistrationDto registrationDto) {

        if (userRepository.existsByEmail(registrationDto.getEmail())) {
            throw new ResourceAlreadyUsedException("User with this email already exists");
        }

        final Role role = roleRepository.findByName(RoleName.ROLE_USER)
                .orElseThrow(() -> new ResourceNotFoundException("Role not found"));

        final User user = new User();
        user.setFirstName(registrationDto.getFirstName());
        user.setLastName(registrationDto.getLastName());
        user.setEmail(registrationDto.getEmail());
        user.setUsername(registrationDto.getUsername());
        user.setPassword(passwordEncoder.encode(registrationDto.getPassword()));
        user.setEnabled(true);
        user.setRole(role);

        return UserDto.toDto(userRepository.save(user));
    }

    @Transactional
    public UserDto updateUser(Long id, UserUpdateDto updateDto) {
        final User user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        user.setFirstName(updateDto.getFirstName());
        user.setLastName(updateDto.getLastName());

        return UserDto.toDto(userRepository.save(user));
    }

    public User getUserByUsername(String username) {
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
    }
}
