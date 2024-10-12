package com.example.tfi_ar.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

import java.time.LocalDate;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "employee")
@SQLDelete(sql = "UPDATE employee SET deleted = true WHERE id = ?")
@Where(clause = "deleted = false")
public class Employee {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;
    @Column(unique = true)
    private String dni;
    private String name;
    @Column(name = "birth_date")
    private LocalDate birthDate;
    @Column(unique = true)
    private String email;
    private String phone;
    @Column(name = "start_date", updatable = false)
    private LocalDate startDate;
    @Column(name = "end_date")
    private LocalDate endDate;
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "created_by", nullable = false)
    private User createdBy;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "updated_by")
    private User updatedBy;
    private boolean deleted;
}
