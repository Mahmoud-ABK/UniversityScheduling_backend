package com.scheduling.universityschedule_backend.model.enums;

public enum AuditAction {
    USER_CREATED,
    USER_UPDATED,
    USER_DELETED,
    USER_LOCKED,
    USER_UNLOCKED,
    PASSWORD_CHANGED,
    PASSWORD_RESET,
    LOGIN_SUCCESS,
    LOGIN_FAILED,
    LOGOUT
}