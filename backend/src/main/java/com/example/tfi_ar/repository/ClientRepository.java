package com.example.tfi_ar.repository;

import com.example.tfi_ar.model.Client;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ClientRepository extends JpaRepository<Client, Integer> {
}
