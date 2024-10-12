package com.example.tfi_ar.model;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum Permission {
    ADMIN_READ("admin:read"),
    ADMIN_WRITE("admin:write"),
    MANAGER_READ("management:read"),
    MANAGER_WRITE("management:write");

    @Getter
    private final String permission;
}
