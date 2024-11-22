package com.example.tfi_ar.dto;

import com.example.tfi_ar.model.Invoice;
import com.example.tfi_ar.model.InvoiceState;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class InvoiceResponse {
    private Integer id;
    private LocalDate issueDate;
    private LocalDate dueDate;
    private String paymentMethod;
    private String observation;
    private InvoiceState state;
    private List<InvoiceDetailResponse> details;

    public InvoiceResponse(Invoice invoice) {
        this.id = invoice.getId();
        this.issueDate = invoice.getIssueDate();
        this.dueDate = invoice.getDueDate();
        this.paymentMethod = invoice.getPaymentMethod();
        this.observation = invoice.getObservation();
        this.state = invoice.getState();
        this.details = invoice.getDetails().stream()
                            .map(InvoiceDetailResponse::new)
                            .collect(Collectors.toList());
    }
}
