package com.example.tfi_ar.service;

import com.example.tfi_ar.dto.EmployeeRequest;
import com.example.tfi_ar.dto.EmployeeResponse;
import com.example.tfi_ar.exception.EmailAlreadyInUseException;
import com.example.tfi_ar.exception.UserAlreadyInUseException;
import com.example.tfi_ar.exception.UserNotFoundException;
import com.example.tfi_ar.model.Employee;
import com.example.tfi_ar.model.User;
import com.example.tfi_ar.repository.EmployeeRepository;
import com.example.tfi_ar.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class EmployeeService {
    private final EmployeeRepository employeeRepository;
    private final UserRepository userRepository;
    private final AuthenticationService authenticationService;

    public EmployeeResponse create(EmployeeRequest request) throws EmailAlreadyInUseException, UserAlreadyInUseException, UserNotFoundException {
        if (employeeRepository.findByEmail(request.getEmail()).isPresent()) {
            throw new EmailAlreadyInUseException("Email already in use");
        }

        if (employeeRepository.findByUserId(request.getUserId()).isPresent()) {
            throw new UserAlreadyInUseException("User already in use");
        }

        User employeeUser = userRepository.findById(request.getUserId())
                .orElseThrow(() -> new UserNotFoundException("User not found"));

        User creatorUser = userRepository.findById(authenticationService.getUserIdFromToken())
                .orElseThrow(() -> new UserNotFoundException("User not found"));

        var employee = Employee.builder()
                .dni(request.getDni())
                .name(request.getName())
                .birthDate(request.getBirthDate())
                .email(request.getEmail())
                .phone(request.getPhone())
                .user(employeeUser)
                .startDate(request.getStartDate())
                .createdBy(creatorUser)
                .build();

        employeeRepository.save(employee);

        return EmployeeResponse.builder()
                .dni(employee.getDni())
                .name(employee.getName())
                .birthDate(employee.getBirthDate())
                .email(employee.getEmail())
                .phone(employee.getPhone())
                .userId(employee.getUser().getId())
                .build();
    }
}
