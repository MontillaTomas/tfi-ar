package com.example.tfi_ar.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SupplierRequest {
    private String cuit;
    private String email;
    private String name;
    private String phone;
    private AddressRequest address;
    private List<PaymentConditionRequest> paymentConditions;
}
