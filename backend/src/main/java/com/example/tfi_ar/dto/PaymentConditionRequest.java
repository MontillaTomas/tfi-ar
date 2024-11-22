package com.example.tfi_ar.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PaymentConditionRequest {
    private String paymentMethod;
    private Integer paymentTermDays;
    private String currency;
    private String bank;
    private String accountNumber;
    private String observation;
}
