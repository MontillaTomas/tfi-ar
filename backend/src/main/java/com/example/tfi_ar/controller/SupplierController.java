package com.example.tfi_ar.controller;

import com.example.tfi_ar.dto.PurchaseRequest;
import com.example.tfi_ar.dto.PurchaseResponse;
import com.example.tfi_ar.dto.SupplierRequest;
import com.example.tfi_ar.dto.SupplierResponse;
import com.example.tfi_ar.exception.*;
import com.example.tfi_ar.service.SupplierService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/suppliers")
@RequiredArgsConstructor
@PreAuthorize("hasRole('ADMIN') or hasRole('SUPPLIER_MODULE_USER')")
public class SupplierController {
    private final SupplierService supplierService;

    @PostMapping
    public ResponseEntity<SupplierResponse> create(@RequestBody SupplierRequest request) throws CuitAlreadyInUseException, EmailAlreadyInUseException, UserNotFoundException, CityNotFoundException, NameAlreadyInUseException {
        return ResponseEntity.status(HttpStatus.CREATED).body(supplierService.create(request));
    }

    @GetMapping("/{id}")
    public ResponseEntity<SupplierResponse> get(@PathVariable Integer id) throws SupplierNotFoundException {
        return ResponseEntity.status(HttpStatus.OK).body(supplierService.get(id));
    }

    @GetMapping
    public ResponseEntity<List<SupplierResponse>> getAll() {
        return ResponseEntity.status(HttpStatus.OK).body(supplierService.getAll());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Integer id) throws SupplierNotFoundException, UserNotFoundException {
        supplierService.delete(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @PutMapping("/{id}")
    public ResponseEntity<SupplierResponse> update(@PathVariable Integer id, @RequestBody SupplierRequest request) throws SupplierNotFoundException, CuitAlreadyInUseException, EmailAlreadyInUseException, CityNotFoundException, NameAlreadyInUseException, UserNotFoundException {
        return ResponseEntity.status(HttpStatus.OK).body(supplierService.update(id, request));
    }

    @PostMapping("/{supplierId}/purchases")
    public ResponseEntity<PurchaseResponse> createPurchase(@PathVariable Integer supplierId, @RequestBody PurchaseRequest request) throws UserNotFoundException, SupplierNotFoundException, PaymentConditionNotFoundException {
        return ResponseEntity.status(HttpStatus.CREATED).body(supplierService.createPurchase(supplierId, request));
    }

    @GetMapping("/{supplierId}/purchases/{id}")
    public ResponseEntity<PurchaseResponse> getPurchase(@PathVariable Integer supplierId, @PathVariable Integer id) throws PurchaseNotFoundException, SupplierNotFoundException {
        return ResponseEntity.status(HttpStatus.OK).body(supplierService.getPurchase(supplierId, id));
    }

    @GetMapping("/{supplierId}/purchases")
    public ResponseEntity<List<PurchaseResponse>> getAllPurchases(@PathVariable Integer supplierId) throws SupplierNotFoundException {
        return ResponseEntity.status(HttpStatus.OK).body(supplierService.getAllPurchases(supplierId));
    }
}
