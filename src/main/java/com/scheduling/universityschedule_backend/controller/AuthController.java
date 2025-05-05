package com.scheduling.universityschedule_backend.controller;

import com.scheduling.universityschedule_backend.dto.authdtos.*;
import com.scheduling.universityschedule_backend.service.AuthenticationService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthenticationService authenticationService;

    public AuthController(AuthenticationService authenticationService) {
        this.authenticationService = authenticationService;
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponseDTO> login(@Valid @RequestBody LoginRequestDTO loginRequest) {
        return ResponseEntity.ok(authenticationService.login(loginRequest));
    }

    @PostMapping("/refresh")
    public ResponseEntity<LoginResponseDTO> refresh(@Valid @RequestBody RefreshTokenRequestDTO refreshRequest) {
        return ResponseEntity.ok(authenticationService.refreshToken(refreshRequest));
    }

    @PostMapping("/logout")
    public ResponseEntity<Void> logout() {
        String token = SecurityContextHolder.getContext().getAuthentication().getCredentials().toString();
        authenticationService.logout(token);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/password/reset")
    public ResponseEntity<Void> resetPassword(@Valid @RequestBody PasswordResetRequestDTO resetRequest) {
        authenticationService.resetPassword(resetRequest);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/password/change")
    public ResponseEntity<Void> changePassword(@Valid @RequestBody PasswordChangeRequestDTO changeRequest) {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        authenticationService.changePassword(username, changeRequest);
        return ResponseEntity.ok().build();
    }
}
