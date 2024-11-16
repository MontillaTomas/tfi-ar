package com.example.tfi_ar.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EmployeeCreateRequest {
    private Integer dni;
    private String name;
    private LocalDate birthDate;
    private String email;
    private String phone;
    private AddressRequest address;
    private LocalDate startDate;
    private LocalDate endDate;
    private Integer userId;
}
