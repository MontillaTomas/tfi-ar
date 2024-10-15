package com.example.tfi_ar.repository;

import com.example.tfi_ar.model.Employee;
import com.example.tfi_ar.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface EmployeeRepository extends JpaRepository<Employee, Integer> {
    Optional<Employee> findByEmail(String email);
    Optional<Employee> findByUserId(Integer userId);
}
