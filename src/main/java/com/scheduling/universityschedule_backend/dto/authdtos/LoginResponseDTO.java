package com.scheduling.universityschedule_backend.dto.authdtos;

import lombok.Data;

@Data
public class LoginResponseDTO {
    private String accessToken;
    private String refreshToken;
}
