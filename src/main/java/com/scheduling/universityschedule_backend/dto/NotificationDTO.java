package com.scheduling.universityschedule_backend.dto;

import lombok.Data;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Email;

@Data
public class NotificationDTO {
    @NotNull
    private String message;

    private String date;

    @NotNull
    private String recepteur;

    @NotNull
    private String expediteur;

    private String type;
}

