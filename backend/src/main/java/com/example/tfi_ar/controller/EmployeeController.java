package com.example.tfi_ar.controller;

import com.example.tfi_ar.dto.EmployeeRequest;
import com.example.tfi_ar.dto.EmployeeResponse;
import com.example.tfi_ar.exception.EmailAlreadyInUseException;
import com.example.tfi_ar.exception.UserAlreadyInUseException;
import com.example.tfi_ar.exception.UserNotFoundException;
import com.example.tfi_ar.service.EmployeeService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/employees")
@RequiredArgsConstructor
@PreAuthorize("hasRole('ADMIN') or hasRole('HR_MODULE_USER')")
public class EmployeeController {
    private final EmployeeService employeeService;

    @PostMapping
    public EmployeeResponse create(@RequestBody EmployeeRequest request) throws UserNotFoundException, UserAlreadyInUseException, EmailAlreadyInUseException {
        return employeeService.create(request);
    }
}
