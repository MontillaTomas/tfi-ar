package com.example.tfi_ar.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Where;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "payment_condition")
@Where(clause = "deleted = false")
public class PaymentCondition {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;

    private String paymentMethod;
    private Integer paymentTermDays;
    private String currency;
    private String bank;
    private String accountNumber;
    private String observation;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "supplier_id", nullable = false)
    private Supplier supplier;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "created_by", nullable = false, updatable = false)
    private User createdBy;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "updated_by")
    private User updatedBy;

    private boolean deleted;
}
