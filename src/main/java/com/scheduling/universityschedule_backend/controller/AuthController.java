package com.scheduling.universityschedule_backend.controller;

import com.scheduling.universityschedule_backend.dto.authdtos.*;
import com.scheduling.universityschedule_backend.service.AuthenticationService;
import com.scheduling.universityschedule_backend.util.CustomLogger;
import jakarta.servlet.http.HttpServletRequest;
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
    private final HttpServletRequest httpServletRequest;

    public AuthController(AuthenticationService authenticationService, HttpServletRequest httpServletRequest) {
        this.authenticationService = authenticationService;
        this.httpServletRequest = httpServletRequest;
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
        try {
            String authHeader = httpServletRequest.getHeader("Authorization");
            if (authHeader == null || !authHeader.startsWith("Bearer ")) {
                CustomLogger.logError("Missing or invalid Authorization header");
                return ResponseEntity.badRequest().build();
            }
            String token = authHeader.substring(7);
            CustomLogger.logInfo("Logged out user with token: " + token);
            authenticationService.logout(token);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            CustomLogger.logError("Logout error: " + e.getMessage());
            return ResponseEntity.badRequest().build();
        }
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