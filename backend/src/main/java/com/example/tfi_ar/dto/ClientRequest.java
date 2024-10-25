package com.example.tfi_ar.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ClientRequest {
    private String name;
    private String email;
    private String phone;
    private String contactName;
    private AddressRequest address;
    private String industry;
    private Integer estimatedTransactionsNumber;
    private String technologiesUsed;
    private String remarks;
}
