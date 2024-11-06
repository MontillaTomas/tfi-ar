package com.example.tfi_ar.repository;

import com.example.tfi_ar.model.Supplier;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface SupplierRepository extends JpaRepository<Supplier, Integer> {
    Optional<Supplier> findByCuit(String cuit);
    Optional<Supplier> findByEmail(String email);
    Optional<Supplier> findByName(String name);
}
