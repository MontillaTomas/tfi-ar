package com.example.tfi_ar.dto;

import com.example.tfi_ar.model.InvoiceDetail;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class InvoiceDetailResponse {
    private Integer id;
    private String description;
    private Double quantity;
    private Double unitPrice;

    public InvoiceDetailResponse(InvoiceDetail detail) {
        this.id = detail.getId();
        this.description = detail.getDescription();
        this.quantity = detail.getQuantity();
        this.unitPrice = detail.getUnitPrice();
    }
}
