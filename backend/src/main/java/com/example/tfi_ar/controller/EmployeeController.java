package com.example.tfi_ar.controller;

import com.example.tfi_ar.dto.EmployeeCreateRequest;
import com.example.tfi_ar.dto.EmployeeResponse;
import com.example.tfi_ar.dto.EmployeeUpdateRequest;
import com.example.tfi_ar.exception.*;
import com.example.tfi_ar.service.EmployeeService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
    public ResponseEntity<EmployeeResponse> create(@RequestBody EmployeeCreateRequest request) throws UserNotFoundException, UserAlreadyInUseException, EmailAlreadyInUseException, DniAlreadyInUseException, CityNotFoundException {
        return ResponseEntity.status(HttpStatus.CREATED).body(employeeService.create(request));
    }

    @GetMapping("/{id}")
    public ResponseEntity<EmployeeResponse> get(@PathVariable Integer id) throws EmployeeNotFoundException {
        return ResponseEntity.status(HttpStatus.OK).body(employeeService.get(id));
    }

    @GetMapping
    public ResponseEntity<List<EmployeeResponse>> getAll() {
        return ResponseEntity.status(HttpStatus.OK).body(employeeService.getAll());
    }


    @PutMapping("/{id}")
    public ResponseEntity<EmployeeResponse> update(@PathVariable Integer id, @RequestBody EmployeeUpdateRequest request) throws EmployeeNotFoundException, UserNotFoundException, EmailAlreadyInUseException, DniAlreadyInUseException, UserAlreadyInUseException, CityNotFoundException {
        return ResponseEntity.status(HttpStatus.OK).body(employeeService.update(id, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Integer id) throws UserNotFoundException, EmployeeNotFoundException {
        employeeService.delete(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}
