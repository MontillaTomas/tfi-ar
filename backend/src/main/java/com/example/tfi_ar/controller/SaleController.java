package com.example.tfi_ar.controller;

import com.example.tfi_ar.dto.SaleRequest;
import com.example.tfi_ar.dto.SaleResponse;
import com.example.tfi_ar.exception.ClientNotFoundException;
import com.example.tfi_ar.exception.SaleNotFoundException;
import com.example.tfi_ar.exception.UserNotFoundException;
import com.example.tfi_ar.service.SaleService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/clients/{clientId}/sales")
@RequiredArgsConstructor
@PreAuthorize("hasRole('ADMIN') or hasRole('SALES_MODULE_USER')")
public class SaleController {
    private final SaleService saleService;

    @PostMapping
    public ResponseEntity<SaleResponse> create(@RequestBody SaleRequest request, @PathVariable Integer clientId) throws UserNotFoundException, ClientNotFoundException {
        return ResponseEntity.status(HttpStatus.CREATED).body(saleService.create(request, clientId));
    }

    @GetMapping("/{saleId}")
    public ResponseEntity<SaleResponse> get(@PathVariable Integer saleId, @PathVariable Integer clientId) throws SaleNotFoundException {
        return ResponseEntity.status(HttpStatus.OK).body(saleService.get(saleId, clientId));
    }

    @GetMapping
    public ResponseEntity<List<SaleResponse>> getAll(@PathVariable Integer clientId) {
        return ResponseEntity.status(HttpStatus.OK).body(saleService.getAll(clientId));
    }

    @PutMapping("/{id}")
    public ResponseEntity<SaleResponse> update(@PathVariable Integer id, @RequestBody SaleRequest request) throws SaleNotFoundException, UserNotFoundException {
        return ResponseEntity.status(HttpStatus.OK).body(saleService.update(id, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Integer id) throws SaleNotFoundException, UserNotFoundException {
        saleService.delete(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}
