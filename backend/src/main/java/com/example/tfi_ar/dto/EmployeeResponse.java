package com.example.tfi_ar.dto;

import com.example.tfi_ar.model.Employee;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EmployeeResponse {
    private Integer id;
    private Integer dni;
    private String name;
    @JsonProperty("birth_date")
    private LocalDate birthDate;
    private String email;
    private String phone;
    private AddressResponse address;
    @JsonProperty("start_date")
    private LocalDate startDate;
    @JsonProperty("end_date")
    private LocalDate endDate;
    @JsonProperty("user_id")
    private Integer userId;

    public EmployeeResponse(Employee employee) {
        this.id = employee.getId();
        this.dni = employee.getDni();
        this.name = employee.getName();
        this.birthDate = employee.getBirthDate();
        this.email = employee.getEmail();
        this.phone = employee.getPhone();
        this.address = new AddressResponse(employee.getAddress());
        this.startDate = employee.getStartDate();
        this.endDate = employee.getEndDate();
        this.userId = employee.getUser().getId();
    }
}
