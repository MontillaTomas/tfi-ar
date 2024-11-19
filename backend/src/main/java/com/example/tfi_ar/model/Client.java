package com.example.tfi_ar.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Where;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "client")
@Where(clause = "deleted = false")
public class Client {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;

    private String name;

    private String email;

    private String phone;

    @Column(name = "contact_name")
    private  String contactName;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "address_id", nullable = false)
    private Address address;

    private  String industry;

    @Column(name = "estimated_transactions_number")
    private Integer estimatedTransactionsNumber;

    @Column(name = "technologies_used")
    private String technologiesUsed;

    @Column(name = "remarks")
    private String remarks;

    @OneToMany(mappedBy = "client", fetch = FetchType.LAZY)
    private List<ClientInteraction> interactions;

    @OneToMany(mappedBy = "client", fetch = FetchType.LAZY)
    private List<Sale> sales;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "created_by", nullable = false, updatable = false)
    private User createdBy;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "updated_by")
    private User updatedBy;
    private boolean deleted;
}
