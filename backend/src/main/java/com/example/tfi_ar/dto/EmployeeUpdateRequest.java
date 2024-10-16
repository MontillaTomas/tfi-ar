package com.example.tfi_ar.dto;

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
public class EmployeeUpdateRequest {
    private Integer dni;
    private String name;
    @JsonProperty("birth_date")
    private LocalDate birthDate;
    private String email;
    private String phone;
    @JsonProperty("address")
    private AddressRequest addressRequest;
    @JsonProperty("start_date")
    private LocalDate startDate;
    @JsonProperty("end_date")
    private LocalDate endDate;
    @JsonProperty("user_id")
    private Integer userId;
}
