package com.example.tfi_ar.dto;

import com.example.tfi_ar.model.PaymentCondition;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PaymentConditionResponse {
    private Integer id;
    private String paymentMethod;
    private Integer paymentTermDays;
    private String currency;
    private String bank;
    private String accountNumber;
    private String observation;

    public PaymentConditionResponse(PaymentCondition paymentCondition) {
        this.id = paymentCondition.getId();
        this.paymentMethod = paymentCondition.getPaymentMethod();
        this.paymentTermDays = paymentCondition.getPaymentTermDays();
        this.currency = paymentCondition.getCurrency();
        this.bank = paymentCondition.getBank();
        this.accountNumber = paymentCondition.getAccountNumber();
        this.observation = paymentCondition.getObservation();
    }
}
