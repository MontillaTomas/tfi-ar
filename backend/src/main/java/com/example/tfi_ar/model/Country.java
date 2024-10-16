package com.example.tfi_ar.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "country")
public class Country {
    @Id
    private Integer id;
    private String name;
    @Column(name = "iso_code", columnDefinition = "char(3)")
    private String isoCode;
    @Column(columnDefinition = "boolean default false")
    private boolean deleted;
}
