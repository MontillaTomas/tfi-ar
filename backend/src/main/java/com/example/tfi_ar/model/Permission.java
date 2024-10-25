package com.example.tfi_ar.model;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum Permission {
    ADMIN_READ("admin:read"),
    ADMIN_WRITE("admin:write"),
    ADMIN_DELETE("admin:delete"),
    ADMIN_UPDATE("admin:update"),

    HR_MODULE_USER_READ("hr_module_user:read"),
    HR_MODULE_USER_WRITE("hr_module_user:write"),
    HR_MODULE_USER_DELETE("hr_module_user:delete"),
    HR_MODULE_USER_UPDATE("hr_module_user:update"),

    SALES_MODULE_USER_READ("sales_module_user:read"),
    SALES_MODULE_USER_WRITE("sales_module_user:write"),
    SALES_MODULE_USER_DELETE("sales_module_user:delete"),
    SALES_MODULE_USER_UPDATE("sales_module_user:update"),

    SUPPLIER_MODULE_USER_READ("supplier_module_user:read"),
    SUPPLIER_MODULE_USER_WRITE("supplier_module_user:write"),
    SUPPLIER_MODULE_USER_DELETE("supplier_module_user:delete"),
    SUPPLIER_MODULE_USER_UPDATE("supplier_module_user:update"),

    CUSTOMER_MODULE_USER_READ("customer_module_user:read"),
    CUSTOMER_MODULE_USER_WRITE("customer_module_user:write"),
    CUSTOMER_MODULE_USER_DELETE("customer_module_user:delete"),
    CUSTOMER_MODULE_USER_UPDATE("customer_module_user:update");

    private final String permission;
}
