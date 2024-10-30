package com.example.tfi_ar.service;

import com.example.tfi_ar.dto.EmployeeCreateRequest;
import com.example.tfi_ar.dto.EmployeeResponse;
import com.example.tfi_ar.dto.EmployeeUpdateRequest;
import com.example.tfi_ar.exception.*;
import com.example.tfi_ar.model.Address;
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
    private final AddressService addressService;

    public EmployeeResponse create(EmployeeCreateRequest request) throws EmailAlreadyInUseException, UserAlreadyInUseException, UserNotFoundException, DniAlreadyInUseException, CityNotFoundException {
        if (employeeRepository.findByDni(request.getDni()).isPresent()) {
            throw new DniAlreadyInUseException("Dni already in use");
        }

        if (employeeRepository.findByEmail(request.getEmail()).isPresent()) {
            throw new EmailAlreadyInUseException("Email already in use");
        }

        User employeeUser = null;

        if (request.getUserId() != null) {
            if (employeeRepository.findByUserId(request.getUserId()).isPresent()) {
                throw new UserAlreadyInUseException("User already in use");
            }

             employeeUser = userRepository.findById(request.getUserId())
                    .orElseThrow(() -> new UserNotFoundException("User not found"));
        }

        User creatorUser = userRepository.findById(authenticationService.getUserIdFromToken())
                .orElseThrow(() -> new UserNotFoundException("User not found"));

        Address address = addressService.create(request.getAddressRequest());

        Employee employee = employeeUser == null
                ? new Employee(request, address, creatorUser)
                : new Employee(request, address, employeeUser, creatorUser);

        Employee savedEmployee = employeeRepository.save(employee);

        return new EmployeeResponse(savedEmployee);
    }

    public EmployeeResponse get(Integer id) throws EmployeeNotFoundException {
        Employee employee = employeeRepository.findById(id)
                .orElseThrow(() -> new EmployeeNotFoundException("Employee not found"));

        return new EmployeeResponse(employee);
    }

    public List<EmployeeResponse> getAll() {
        return employeeRepository.findAll().stream()
                .map(EmployeeResponse::new)
                .toList();
    }

    public void delete(Integer id) throws EmployeeNotFoundException, UserNotFoundException {
        Employee employee = employeeRepository.findById(id)
                .orElseThrow(() -> new EmployeeNotFoundException("Employee not found"));

        User updateUser = userRepository.findById(authenticationService.getUserIdFromToken())
                .orElseThrow(() -> new UserNotFoundException("User not found"));

        employee.setUpdatedBy(updateUser);
        employee.getAddress().setDeleted(true);
        employee.getAddress().setUpdatedBy(updateUser);
        employee.setUser(null);
        employee.setDeleted(true);

        employeeRepository.save(employee);
    }

    public EmployeeResponse update(Integer id, EmployeeUpdateRequest request) throws EmployeeNotFoundException, DniAlreadyInUseException, EmailAlreadyInUseException, UserAlreadyInUseException, UserNotFoundException, CityNotFoundException {
        Employee employee = employeeRepository.findById(id)
                .orElseThrow(() -> new EmployeeNotFoundException("Employee not found"));

        if (request.getDni() != null && employeeRepository.findByDni(request.getDni()).isPresent() && !employee.getDni().equals(request.getDni())) {
            throw new DniAlreadyInUseException("Dni already in use");
        }

        if(request.getEmail() != null && employeeRepository.findByEmail(request.getEmail()).isPresent() && !employee.getEmail().equals(request.getEmail())) {
            throw new EmailAlreadyInUseException("Email already in use");
        }

        User updateUser = userRepository.findById(authenticationService.getUserIdFromToken())
                .orElseThrow(() -> new UserNotFoundException("User not found"));

        User employeeUser = null;
        if(request.getUserId() != null) {
            employeeUser = userRepository.findById(request.getUserId())
                    .orElseThrow(() -> new UserNotFoundException("User not found"));
        }

        addressService.update(employee.getAddress().getId(), request.getAddressRequest());

        employee.setDni(request.getDni());
        employee.setName(request.getName());
        employee.setBirthDate(request.getBirthDate());
        employee.setEmail(request.getEmail());
        employee.setPhone(request.getPhone());
        employee.setStartDate(request.getStartDate());
        employee.setEndDate(request.getEndDate());
        employee.setUser(employeeUser);
        employee.setUpdatedBy(updateUser);

        Employee updatedEmployee = employeeRepository.save(employee);

        return new EmployeeResponse(updatedEmployee);
    }
}
