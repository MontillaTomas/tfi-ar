package com.example.tfi_ar.controller;

import com.example.tfi_ar.dto.ClientRequest;
import com.example.tfi_ar.dto.ClientResponse;
import com.example.tfi_ar.exception.CityNotFoundException;
import com.example.tfi_ar.exception.ClientNotFoundException;
import com.example.tfi_ar.exception.UserNotFoundException;
import com.example.tfi_ar.service.ClientService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/clients")
@RequiredArgsConstructor
@PreAuthorize("hasRole('ADMIN') or hasRole('CLIENT_MODULE_USER')")

public class ClientController {
    private final ClientService clientService;

    @PostMapping
    public ResponseEntity<ClientResponse> create(@RequestBody ClientRequest request) throws UserNotFoundException, CityNotFoundException {
        return ResponseEntity.status(HttpStatus.CREATED).body(clientService.create(request));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ClientResponse> get(@PathVariable Integer id) throws ClientNotFoundException {
        return ResponseEntity.status(HttpStatus.OK).body(clientService.get(id));
    }

    @GetMapping
    public ResponseEntity<List<ClientResponse>> getAll() {
        return ResponseEntity.status(HttpStatus.OK).body(clientService.getAll());
    }

    @PutMapping("/{id}")
    public ResponseEntity<ClientResponse> update(@PathVariable Integer id, @RequestBody ClientRequest request) throws ClientNotFoundException, UserNotFoundException, CityNotFoundException {
        return ResponseEntity.status(HttpStatus.OK).body(clientService.update(id, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Integer id) throws ClientNotFoundException, UserNotFoundException {
        clientService.delete(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}
