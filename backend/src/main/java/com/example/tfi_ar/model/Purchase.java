package com.example.tfi_ar.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Where;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "purchase")
@Where(clause = "deleted = false")
public class Purchase {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;

    @Column(name = "purchase_date")
    private LocalDateTime purchaseDate;

    private Double total;
    private String observation;

    @OneToMany(fetch = FetchType.LAZY,
               mappedBy = "purchase",
               cascade = CascadeType.ALL)
    private List<PurchaseRating> ratings;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "supplier_id", nullable = false)
    private Supplier supplier;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "payment_condition_id", nullable = false)
    private PaymentCondition paymentCondition;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "created_by", nullable = false, updatable = false)
    private User createdBy;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "updated_by")
    private User updatedBy;

    private boolean deleted;

    public PurchaseRating getPurchaseRating() {
        if (ratings == null) return null;
        return ratings.stream().filter(rating -> !rating.isDeleted()).findFirst().orElse(null);
    }
}
