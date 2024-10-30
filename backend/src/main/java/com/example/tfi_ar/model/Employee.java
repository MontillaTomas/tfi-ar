package com.example.tfi_ar.model;

import com.example.tfi_ar.dto.EmployeeCreateRequest;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Where;

import java.time.LocalDate;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "employee")
@Where(clause = "deleted = false")
public class Employee {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;
    private Integer dni;
    private String name;
    @Column(name = "birth_date")
    private LocalDate birthDate;
    private String email;
    private String phone;
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "address_id", nullable = false)
    private Address address;
    @Column(name = "start_date")
    private LocalDate startDate;
    @Column(name = "end_date")
    private LocalDate endDate;
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = true)
    private User user;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "created_by", nullable = false, updatable = false)
    private User createdBy;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "updated_by")
    private User updatedBy;
    private boolean deleted;

    public Employee(EmployeeCreateRequest request,
                    Address address,
                    User employeeUser,
                    User creatorUser) {
        this.dni = request.getDni();
        this.name = request.getName();
        this.birthDate = request.getBirthDate();
        this.email = request.getEmail();
        this.phone = request.getPhone();
        this.user = employeeUser;
        this.address = address;
        this.startDate = request.getStartDate();
        this.endDate = request.getEndDate();
        this.createdBy = creatorUser;
    }

    public Employee(EmployeeCreateRequest request,
                    Address address,
                    User creatorUser) {
        this.dni = request.getDni();
        this.name = request.getName();
        this.birthDate = request.getBirthDate();
        this.email = request.getEmail();
        this.phone = request.getPhone();
        this.address = address;
        this.startDate = request.getStartDate();
        this.endDate = request.getEndDate();
        this.createdBy = creatorUser;
    }
}
