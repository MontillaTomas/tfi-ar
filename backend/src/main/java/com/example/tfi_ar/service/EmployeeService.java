package com.example.tfi_ar.service;

import com.example.tfi_ar.dto.EmployeeRequest;
import com.example.tfi_ar.dto.EmployeeResponse;
import com.example.tfi_ar.exception.DniAlreadyInUseException;
import com.example.tfi_ar.exception.EmailAlreadyInUseException;
import com.example.tfi_ar.exception.UserAlreadyInUseException;
import com.example.tfi_ar.exception.UserNotFoundException;
import com.example.tfi_ar.model.Employee;
import com.example.tfi_ar.model.User;
import com.example.tfi_ar.repository.EmployeeRepository;
import com.example.tfi_ar.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class EmployeeService {
    private final EmployeeRepository employeeRepository;
    private final UserRepository userRepository;
    private final AuthenticationService authenticationService;

    public EmployeeResponse create(EmployeeRequest request) throws EmailAlreadyInUseException, UserAlreadyInUseException, UserNotFoundException, DniAlreadyInUseException {
        if (employeeRepository.findByDni(request.getDni()).isPresent()) {
            throw new DniAlreadyInUseException("Dni already in use");
        }

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
                .endDate(request.getEndDate())
                .createdBy(creatorUser)
                .build();

        Integer employeeId = employeeRepository.save(employee).getId();

        return EmployeeResponse.builder()
                .id(employeeId)
                .dni(request.getDni())
                .name(request.getName())
                .birthDate(request.getBirthDate())
                .email(request.getEmail())
                .phone(request.getPhone())
                .startDate(request.getStartDate())
                .endDate(request.getEndDate())
                .userId(employee.getUser().getId())
                .build();
    }

    public EmployeeResponse get(Integer id) throws UserNotFoundException {
        Employee employee = employeeRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException("User not found"));

        return EmployeeResponse.builder()
                .id(employee.getId())
                .dni(employee.getDni())
                .name(employee.getName())
                .birthDate(employee.getBirthDate())
                .email(employee.getEmail())
                .phone(employee.getPhone())
                .startDate(employee.getStartDate())
                .endDate(employee.getEndDate())
                .userId(employee.getUser().getId())
                .build();
    }

    public List<EmployeeResponse> getAll() {
        return employeeRepository.findAll().stream()
                .map(employee -> EmployeeResponse.builder()
                        .id(employee.getId())
                        .dni(employee.getDni())
                        .name(employee.getName())
                        .birthDate(employee.getBirthDate())
                        .email(employee.getEmail())
                        .phone(employee.getPhone())
                        .startDate(employee.getStartDate())
                        .endDate(employee.getEndDate())
                        .userId(employee.getUser().getId())
                        .build())
                .toList();
    }
}
