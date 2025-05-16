package com.example.demospring1.controller;

import com.example.demospring1.service.UserService;
import com.example.demospring1.service.dto.UserDto;
import com.example.demospring1.service.dto.UserRegistrationDto;
import com.example.demospring1.service.dto.UserUpdateDto;
import com.example.demospring1.service.enums.RoleName;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/users")
public class UserController {

    private final UserService userService;

    @GetMapping("/{id}")
    @PreAuthorize("hasAuthority('VIEW_USER')")
    public ResponseEntity<UserDto> getUserById(@PathVariable Long id) {
        return ResponseEntity.ok(userService.getUserDtoById(id));
    }

    @PostMapping
    @PreAuthorize("hasAuthority('CREATE_USER')")
    public ResponseEntity<UserDto> createUser(@Valid @RequestBody UserRegistrationDto userRegistrationDto) {
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(userService.createUser(userRegistrationDto));
    }


    @PutMapping("/{id}")
    @PreAuthorize("hasAuthority('UPDATE_USER')")
    public ResponseEntity<UserDto> updateUser(@PathVariable Long id, @Valid @RequestBody UserUpdateDto updateDto) {
        return ResponseEntity.ok(userService.updateUser(id, updateDto));
    }

    @PutMapping("/{userId}/assign-role")
    @PreAuthorize("hasAuthority('ASSIGN_ROLE')")
    public ResponseEntity<String> assignRoleToUser(
            @PathVariable Long userId,
            @RequestParam RoleName roleName
    ) {
        userService.assignRole(userId, roleName);
        return ResponseEntity.ok("Role assigned successfully.");
    }

}
