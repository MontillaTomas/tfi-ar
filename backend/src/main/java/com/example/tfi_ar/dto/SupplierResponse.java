package com.example.tfi_ar.dto;

import com.example.tfi_ar.model.Supplier;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SupplierResponse {
    private Integer id;
    private String cuit;
    private String email;
    private String name;
    private String phone;
    private AddressResponse address;
    private List<PaymentConditionResponse> paymentConditions;

    public SupplierResponse(Supplier supplier) {
        this.id = supplier.getId();
        this.cuit = supplier.getCuit();
        this.email = supplier.getEmail();
        this.name = supplier.getName();
        this.phone = supplier.getPhone();
        this.address = new AddressResponse(supplier.getAddress());
        this.paymentConditions = Optional.ofNullable(supplier.getPaymentConditions())
                .orElseGet(Collections::emptyList)
                .stream()
                .map(PaymentConditionResponse::new)
                .collect(Collectors.toList());
    }
}