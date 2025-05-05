package com.scheduling.universityschedule_backend.service;

import com.scheduling.universityschedule_backend.dto.authdtos.*;
import com.scheduling.universityschedule_backend.model.AuditLog;
import com.scheduling.universityschedule_backend.model.UserCredentials;
import com.scheduling.universityschedule_backend.model.enums.AuditAction;
import com.scheduling.universityschedule_backend.model.enums.UserStatus;
import com.scheduling.universityschedule_backend.repository.AuditLogRepository;
import com.scheduling.universityschedule_backend.repository.UserCredentialsRepository;
import com.scheduling.universityschedule_backend.security.JwtUtil;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
public class AuthenticationService {

    private final AuthenticationManager authenticationManager;
    private final UserCredentialsRepository userCredentialsRepository;
    private final AuditLogRepository auditLogRepository;
    private final JwtUtil jwtUtil;
    private final PasswordEncoder passwordEncoder;

    public AuthenticationService(
            AuthenticationManager authenticationManager,
            UserCredentialsRepository userCredentialsRepository,
            AuditLogRepository auditLogRepository,
            JwtUtil jwtUtil,
            PasswordEncoder passwordEncoder) {
        this.authenticationManager = authenticationManager;
        this.userCredentialsRepository = userCredentialsRepository;
        this.auditLogRepository = auditLogRepository;
        this.jwtUtil = jwtUtil;
        this.passwordEncoder = passwordEncoder;
    }

    @Transactional
    public LoginResponseDTO login(LoginRequestDTO loginRequest) {
        UserCredentials credentials = userCredentialsRepository.findByUsername(loginRequest.getUsername())
                .orElseThrow(() -> new BadCredentialsException("Invalid username or password"));

        if (credentials.getStatus() == UserStatus.LOCKED) {
            if (credentials.getLockTime().plusMinutes(15).isAfter(LocalDateTime.now())) {
                auditLogRepository.save(AuditLog.builder()
                        .userLogin(loginRequest.getUsername())
                        .action(AuditAction.LOGIN_FAILED.name())
                        .details("Account locked")
                        .timestamp(LocalDateTime.now())
                        .build());
                throw new BadCredentialsException("Account is locked. Try again later.");
            } else {
                credentials.setStatus(UserStatus.ACTIVE);
                credentials.setFailedAttempts(0);
                credentials.setLockTime(null);
                userCredentialsRepository.save(credentials);
            }
        }

        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            loginRequest.getUsername(),
                            loginRequest.getPassword()
                    )
            );

            credentials.setFailedAttempts(0);
            userCredentialsRepository.save(credentials);

            String accessToken = jwtUtil.generateAccessToken(
                    credentials.getUsername(),
                    credentials.getRole().getAuthority()
            );
            String refreshToken = jwtUtil.generateRefreshToken(credentials.getUsername());

            auditLogRepository.save(AuditLog.builder()
                    .userLogin(loginRequest.getUsername())
                    .action(AuditAction.LOGIN_SUCCESS.name())
                    .timestamp(LocalDateTime.now())
                    .build());

            LoginResponseDTO response = new LoginResponseDTO();
            response.setAccessToken(accessToken);
            response.setRefreshToken(refreshToken);
            return response;

        } catch (BadCredentialsException e) {
            int failedAttempts = credentials.getFailedAttempts() + 1;
            credentials.setFailedAttempts(failedAttempts);
            if (failedAttempts >= 5) {
                credentials.setStatus(UserStatus.LOCKED);
                credentials.setLockTime(LocalDateTime.now());
            }
            userCredentialsRepository.save(credentials);

            auditLogRepository.save(AuditLog.builder()
                    .userLogin(loginRequest.getUsername())
                    .action(AuditAction.LOGIN_FAILED.name())
                    .details("Invalid password")
                    .timestamp(LocalDateTime.now())
                    .build());

            throw new BadCredentialsException("Invalid username or password");
        }
    }

    @Transactional
    public LoginResponseDTO refreshToken(RefreshTokenRequestDTO request) {
        String username = jwtUtil.extractUsername(request.getRefreshToken());
        if (!jwtUtil.isTokenValid(request.getRefreshToken(), username)) {
            throw new IllegalArgumentException("Invalid or expired refresh token");
        }

        UserCredentials credentials = userCredentialsRepository.findByUsername(username)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));

        if (credentials.getStatus() != UserStatus.ACTIVE) {
            throw new IllegalArgumentException("User account is not active");
        }

        String accessToken = jwtUtil.generateAccessToken(
                credentials.getUsername(),
                credentials.getRole().getAuthority()
        );
        String refreshToken = jwtUtil.generateRefreshToken(credentials.getUsername());

        auditLogRepository.save(AuditLog.builder()
                .userLogin(username)
                .action(AuditAction.LOGIN_SUCCESS.name())
                .details("Token refreshed")
                .timestamp(LocalDateTime.now())
                .build());

        LoginResponseDTO response = new LoginResponseDTO();
        response.setAccessToken(accessToken);
        response.setRefreshToken(refreshToken);
        return response;
    }

    @Transactional
    public void logout(String token) {
        String username = jwtUtil.extractUsername(token);
        auditLogRepository.save(AuditLog.builder()
                .userLogin(username)
                .action(AuditAction.LOGOUT.name())
                .timestamp(LocalDateTime.now())
                .build());
        // Note: Token blacklisting can be added here if needed (e.g., store in Redis)
    }

    @Transactional
    public void resetPassword(PasswordResetRequestDTO request) {
        // Placeholder for email-based password reset
        // 1. Find user by email (requires Personne entity with email field)
        // 2. Generate reset token (e.g., UUID or JWT with short expiration)
        // 3. Send email with reset link (requires email service integration)
        // 4. Log action
        auditLogRepository.save(AuditLog.builder()
                .userLogin(request.getEmail())
                .action(AuditAction.PASSWORD_RESET.name())
                .details("Password reset requested")
                .timestamp(LocalDateTime.now())
                .build());
        throw new UnsupportedOperationException("Password reset not implemented yet");
    }

    @Transactional
    public void changePassword(String username, PasswordChangeRequestDTO request) {
        UserCredentials credentials = userCredentialsRepository.findByUsername(username)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));

        if (!passwordEncoder.matches(request.getOldPassword(), credentials.getPassword())) {
            auditLogRepository.save(AuditLog.builder()
                    .userLogin(username)
                    .action(AuditAction.PASSWORD_CHANGED.name())
                    .details("Failed: Invalid old password")
                    .timestamp(LocalDateTime.now())
                    .build());
            throw new IllegalArgumentException("Invalid old password");
        }

        credentials.setPassword(passwordEncoder.encode(request.getNewPassword()));
        credentials.setLastPasswordChange(LocalDateTime.now());
        credentials.setFailedAttempts(0);
        userCredentialsRepository.save(credentials);

        auditLogRepository.save(AuditLog.builder()
                .userLogin(username)
                .action(AuditAction.PASSWORD_CHANGED.name())
                .timestamp(LocalDateTime.now())
                .build());
    }
}
