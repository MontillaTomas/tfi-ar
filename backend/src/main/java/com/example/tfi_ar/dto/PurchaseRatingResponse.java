package com.example.tfi_ar.dto;

import com.example.tfi_ar.model.PurchaseRating;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PurchaseRatingResponse {
    private Integer id;
    private Integer rating;
    private String observation;

    public PurchaseRatingResponse(PurchaseRating purchaseRating) {
        this.id = purchaseRating.getId();
        this.rating = purchaseRating.getRating();
        this.observation = purchaseRating.getObservation();
    }
}
