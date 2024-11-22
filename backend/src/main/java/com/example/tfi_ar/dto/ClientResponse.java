package com.example.tfi_ar.dto;

import com.example.tfi_ar.model.Client;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ClientResponse {
    private Integer id;
    private String name;
    private String email;
    private String phone;
    private String contactName;
    private AddressResponse address;
    private String industry;
    private Integer estimatedTransactionsNumber;
    private String technologiesUsed;
    private String remarks;
    private List<ClientInteractionResponse> interactions;
    private double totalAmountSales;

    public ClientResponse(Client client) {
        this.id = client.getId();
        this.name = client.getName();
        this.email = client.getEmail();
        this.phone = client.getPhone();
        this.contactName = client.getContactName();
        this.address = new AddressResponse(client.getAddress());
        this.industry = client.getIndustry();
        this.estimatedTransactionsNumber = client.getEstimatedTransactionsNumber();
        this.technologiesUsed = client.getTechnologiesUsed();
        this.remarks = client.getRemarks();

        if(client.getInteractions() != null && !client.getInteractions().isEmpty()) {
            this.interactions = client.getInteractions().stream()
                    .map(ClientInteractionResponse::new)
                    .collect(java.util.stream.Collectors.toList());
        } else {
            this.interactions = new ArrayList<>();
        }

        if(client.getSales() != null) {
            client.getSales().stream()
                    .map(sale -> sale.getInvoice().getDetails())
                    .forEach(invoiceDetails -> {
                        invoiceDetails.forEach(invoiceDetail -> {
                            this.totalAmountSales += invoiceDetail.getQuantity() * invoiceDetail.getUnitPrice();
                        });
                    });
            return;
        }

        this.totalAmountSales = 0;
    }
}
