package com.example.tfi_ar.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SaleCreateRequest {
    private LocalDateTime saleDate;
    private String observation;
    private InvoiceCreateRequest invoice;
}
