package com.example.tfi_ar.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AddressRequest {
    private String street;
    private String number;
    private String floor;
    private String apartment;
    @JsonProperty("postal_code")
    private String postalCode;
    @JsonProperty("city_id")
    private Integer cityId;
    private String observations;
}
