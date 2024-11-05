package com.example.tfi_ar.dto;

import com.example.tfi_ar.model.Purchase;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PurchaseResponse {
    private Integer id;
    private LocalDateTime purchaseDate;
    private Double total;
    private String observation;
    private PurchaseRatingResponse purchaseRating;
    private PaymentConditionResponse paymentCondition;

    public PurchaseResponse(Purchase purchase) {
        this.id = purchase.getId();
        this.purchaseDate = purchase.getPurchaseDate();
        this.total = purchase.getTotal();
        this.observation = purchase.getObservation();
        this.purchaseRating = purchase.getPurchaseRating() != null ?
                new PurchaseRatingResponse(purchase.getPurchaseRating()) : null;
        this.paymentCondition = new PaymentConditionResponse(purchase.getPaymentCondition());
    }
}
