package com.example.tfi_ar.repository;

import com.example.tfi_ar.model.Address;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AddressRepository extends JpaRepository<Address, Integer> {
}
