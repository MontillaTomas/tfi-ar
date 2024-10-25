package com.example.tfi_ar.controller;

import com.example.tfi_ar.dto.EmployeeCreateRequest;
import com.example.tfi_ar.dto.EmployeeResponse;
import com.example.tfi_ar.dto.EmployeeUpdateRequest;
import com.example.tfi_ar.exception.*;
import com.example.tfi_ar.service.EmployeeService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/employees")
@RequiredArgsConstructor
@PreAuthorize("hasRole('ADMIN') or hasRole('HR_MODULE_USER')")
public class EmployeeController {
    private final EmployeeService employeeService;

    @PostMapping
    public EmployeeResponse create(@RequestBody EmployeeCreateRequest request) throws UserNotFoundException, UserAlreadyInUseException, EmailAlreadyInUseException, DniAlreadyInUseException, CityNotFoundException {
        return employeeService.create(request);
    }

    @GetMapping("/{id}")
    public EmployeeResponse get(@PathVariable Integer id) throws EmployeeNotFoundException {
        return employeeService.get(id);
    }

    @GetMapping
    public List<EmployeeResponse> getAll() {
        return employeeService.getAll();
    }


    @PutMapping("/{id}")
    public EmployeeResponse update(@PathVariable Integer id, @RequestBody EmployeeUpdateRequest request) throws EmployeeNotFoundException, UserNotFoundException, EmailAlreadyInUseException, DniAlreadyInUseException, UserAlreadyInUseException, CityNotFoundException {
        return employeeService.update(id, request);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Integer id) throws UserNotFoundException, EmployeeNotFoundException {
        employeeService.delete(id);
    }
}
