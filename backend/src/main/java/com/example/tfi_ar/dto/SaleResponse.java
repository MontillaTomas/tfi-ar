package com.example.tfi_ar.dto;

import com.example.tfi_ar.model.Sale;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SaleResponse {
    private Integer id;
    private String saleDate;
    private String observation;
    private InvoiceResponse invoice;

    public SaleResponse(Sale sale) {
        this.id = sale.getId();
        this.saleDate = sale.getSaleDate().toString();
        this.observation = sale.getObservation();
        this.invoice = new InvoiceResponse(sale.getInvoice());
    }
}
