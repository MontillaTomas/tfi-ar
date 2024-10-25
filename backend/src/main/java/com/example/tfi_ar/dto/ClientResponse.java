package com.example.tfi_ar.dto;

import com.example.tfi_ar.model.Client;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

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
    }
}
