package com.example.tfi_ar.model;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@RequiredArgsConstructor
public enum Role {
    HR_MODULE_USER(Set.of(
            Permission.HR_MODULE_USER_READ,
            Permission.HR_MODULE_USER_WRITE,
            Permission.HR_MODULE_USER_DELETE,
            Permission.HR_MODULE_USER_UPDATE
    )),
    SALES_MODULE_USER(Set.of(
            Permission.SALES_MODULE_USER_READ,
            Permission.SALES_MODULE_USER_WRITE,
            Permission.SALES_MODULE_USER_DELETE,
            Permission.SALES_MODULE_USER_UPDATE
    )),
    SUPPLIER_MODULE_USER(Set.of(
            Permission.SUPPLIER_MODULE_USER_READ,
            Permission.SUPPLIER_MODULE_USER_WRITE,
            Permission.SUPPLIER_MODULE_USER_DELETE,
            Permission.SUPPLIER_MODULE_USER_UPDATE
    )),
    CUSTOMER_MODULE_USER(Set.of(
            Permission.CUSTOMER_MODULE_USER_READ,
            Permission.CUSTOMER_MODULE_USER_WRITE,
            Permission.CUSTOMER_MODULE_USER_DELETE,
            Permission.CUSTOMER_MODULE_USER_UPDATE
    )),
    ADMIN(Set.of(
            Permission.HR_MODULE_USER_READ,
            Permission.HR_MODULE_USER_WRITE,
            Permission.HR_MODULE_USER_DELETE,
            Permission.HR_MODULE_USER_UPDATE,

            Permission.SALES_MODULE_USER_READ,
            Permission.SALES_MODULE_USER_WRITE,
            Permission.SALES_MODULE_USER_DELETE,
            Permission.SALES_MODULE_USER_UPDATE,

            Permission.SUPPLIER_MODULE_USER_READ,
            Permission.SUPPLIER_MODULE_USER_WRITE,
            Permission.SUPPLIER_MODULE_USER_DELETE,
            Permission.SUPPLIER_MODULE_USER_UPDATE,

            Permission.CUSTOMER_MODULE_USER_READ,
            Permission.CUSTOMER_MODULE_USER_WRITE,
            Permission.CUSTOMER_MODULE_USER_DELETE,
            Permission.CUSTOMER_MODULE_USER_UPDATE,

            Permission.ADMIN_READ,
            Permission.ADMIN_WRITE,
            Permission.ADMIN_DELETE,
            Permission.ADMIN_UPDATE
    ));

    @Getter
    private final Set<Permission> permissions;

    public List<SimpleGrantedAuthority> getAuthorities() {
        var authorities = getPermissions()
                .stream()
                .map(permission -> new SimpleGrantedAuthority(permission.getPermission()))
                .collect(Collectors.toList());
        authorities.add(new SimpleGrantedAuthority("ROLE_" + this.name()));
        return authorities;
    }
}
