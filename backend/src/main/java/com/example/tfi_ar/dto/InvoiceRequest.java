package com.example.tfi_ar.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class InvoiceRequest {
    private LocalDate issueDate;
    private LocalDate dueDate;
    private String paymentMethod;
    private String observation;
    private List<InvoiceDetailRequest> details;
}