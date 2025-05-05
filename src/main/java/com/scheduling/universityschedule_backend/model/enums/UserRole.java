package com.scheduling.universityschedule_backend.model.enums;

public enum UserRole {
    ROLE_ADMIN,
    ROLE_TECHNICIAN,
    ROLE_TEACHER,
    ROLE_STUDENT;

    public String getAuthority() {
        return this.name();
    }
}