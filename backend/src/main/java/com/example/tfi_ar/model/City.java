package com.example.tfi_ar.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "city")
public class City {
    @Id
    private Integer id;
    private String name;
    @ManyToOne
    @JoinColumn(name = "state_id", nullable = false)
    private State state;
    @Column(columnDefinition = "boolean default false")
    private boolean deleted;
}
