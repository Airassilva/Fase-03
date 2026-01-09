package dev.aira.agendamento.user.controller;

import dev.aira.agendamento.user.dtos.*;
import dev.aira.agendamento.user.entities.User;
import dev.aira.agendamento.user.mapper.UserMapper;
import dev.aira.agendamento.user.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;
    private final UserMapper  userMapper;

    @PostMapping
    public ResponseEntity<UserResponse> createUser(@Valid @RequestBody UserRequest userRequest) {
        User usuarioEntity = userMapper.toEntity(userRequest);
        User usuarioSalvo = userService.create(usuarioEntity);
        return ResponseEntity.status(HttpStatus.CREATED)
                                .body(userMapper.toResponse(usuarioSalvo));
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@Valid @RequestBody LoginRequest  loginRequest) {
        return ResponseEntity.ok(userService.login(loginRequest));
    }

    @PutMapping("/{id}")
    public ResponseEntity<UserResponse> update(@Valid @RequestBody UserUpdateRequest updateRequest, @PathVariable UUID id) {
        User userUpdated = userService.update(id, updateRequest);
        return ResponseEntity.ok(userMapper.toResponse(userUpdated));
    }

    @PreAuthorize("hasRole('SERVICE')")
    @GetMapping("/{id}")
    public ResponseEntity<UserResponse> findById(@PathVariable UUID id) {
        User user = userService.findById(id);
        return ResponseEntity.ok(userMapper.toResponse(user));
    }

    @GetMapping
    public ResponseEntity<Page<UserResponse>> findAll(Pageable pageable) {
        Page<User> users = userService.findAll(pageable);
        Page<UserResponse> response = userMapper.pageEntityToPageResponse(users);
        return ResponseEntity.ok(response);
    }

    @PatchMapping("/{id}/deactivate")
    public ResponseEntity<Void> deactivateUser(@PathVariable UUID id) {
        userService.desactiveUser(id);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{id}/activate")
    public ResponseEntity<Void> activateUser(@PathVariable UUID id) {
        userService.activateUser(id);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable UUID id) {
        userService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
