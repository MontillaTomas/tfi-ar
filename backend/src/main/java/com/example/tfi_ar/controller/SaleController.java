package com.example.tfi_ar.controller;

import com.example.tfi_ar.dto.SaleCreateRequest;
import com.example.tfi_ar.dto.SaleResponse;
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
@RequestMapping("/api/v1/sales")
@RequiredArgsConstructor
@PreAuthorize("hasRole('ADMIN') or hasRole('SALES_MODULE_USER')")
public class SaleController {
    private final SaleService saleService;

    @PostMapping
    public ResponseEntity<SaleResponse> create(@RequestBody SaleCreateRequest request) throws UserNotFoundException {
        return ResponseEntity.status(HttpStatus.CREATED).body(saleService.create(request));
    }

    @GetMapping("/{id}")
    public ResponseEntity<SaleResponse> get(@PathVariable Integer id) throws SaleNotFoundException {
        return ResponseEntity.status(HttpStatus.OK).body(saleService.get(id));
    }

    @GetMapping
    public ResponseEntity<List<SaleResponse>> getAll() {
        return ResponseEntity.status(HttpStatus.OK).body(saleService.getAll());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Integer id) throws SaleNotFoundException, UserNotFoundException {
        saleService.delete(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}
