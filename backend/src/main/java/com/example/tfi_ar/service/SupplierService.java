package com.example.tfi_ar.service;

import com.example.tfi_ar.dto.SupplierRequest;
import com.example.tfi_ar.dto.SupplierResponse;
import com.example.tfi_ar.exception.*;
import com.example.tfi_ar.model.PaymentCondition;
import com.example.tfi_ar.model.Supplier;
import com.example.tfi_ar.model.User;
import com.example.tfi_ar.repository.SupplierRepository;
import com.example.tfi_ar.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class SupplierService {
    private final SupplierRepository supplierRepository;
    private final UserRepository userRepository;
    private final AuthenticationService authenticationService;
    private final AddressService addressService;

    public SupplierResponse create(SupplierRequest request) throws CuitAlreadyInUseException, EmailAlreadyInUseException, UserNotFoundException, CityNotFoundException, NameAlreadyInUseException {
        if (supplierRepository.findByCuit(request.getCuit()).isPresent()) {
            throw new CuitAlreadyInUseException("Cuit already in use");
        }

        if (supplierRepository.findByEmail(request.getEmail()).isPresent()) {
            throw new EmailAlreadyInUseException("Email already in use");
        }

        if (supplierRepository.findByName(request.getName()).isPresent()) {
            throw new NameAlreadyInUseException("Name already in use");
        }

        User creatorUser = userRepository.findById(authenticationService.getUserIdFromToken())
                .orElseThrow(() -> new UserNotFoundException("User not found"));

        Supplier supplier = Supplier.builder()
                .cuit(request.getCuit())
                .email(request.getEmail())
                .name(request.getName())
                .phone(request.getPhone())
                .address(addressService.create(request.getAddress()))
                .createdBy(creatorUser)
                .build();

        List<PaymentCondition> paymentConditions = new ArrayList<>();
        request.getPaymentConditions().forEach(paymentCondition -> {
            paymentConditions.add(
                    PaymentCondition.builder()
                            .paymentMethod(paymentCondition.getPaymentMethod())
                            .paymentTermDays(paymentCondition.getPaymentTermDays())
                            .currency(paymentCondition.getCurrency())
                            .bank(paymentCondition.getBank())
                            .accountNumber(paymentCondition.getAccountNumber())
                            .observation(paymentCondition.getObservation())
                            .supplier(supplier)
                            .createdBy(creatorUser)
                            .build()
            );
        });
        supplier.setPaymentConditions(paymentConditions);

        Supplier savedSupplier = supplierRepository.save(supplier);

        return new SupplierResponse(savedSupplier);
    }
}
