package com.example.tfi_ar.service;

import com.example.tfi_ar.dto.PurchaseRequest;
import com.example.tfi_ar.dto.PurchaseResponse;
import com.example.tfi_ar.dto.SupplierRequest;
import com.example.tfi_ar.dto.SupplierResponse;
import com.example.tfi_ar.exception.*;
import com.example.tfi_ar.model.PaymentCondition;
import com.example.tfi_ar.model.Purchase;
import com.example.tfi_ar.model.Supplier;
import com.example.tfi_ar.model.User;
import com.example.tfi_ar.repository.PurchaseRepository;
import com.example.tfi_ar.repository.SupplierRepository;
import com.example.tfi_ar.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class SupplierService {
    private final SupplierRepository supplierRepository;
    private final PurchaseRepository purchaseRepository;
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

    public SupplierResponse get(Integer id) throws SupplierNotFoundException {
        Supplier supplier = supplierRepository.findById(id)
                .orElseThrow(() -> new SupplierNotFoundException("Supplier not found"));
        return new SupplierResponse(supplier);
    }

    public List<SupplierResponse> getAll() {
        return supplierRepository.findAll().stream()
                .map(SupplierResponse::new)
                .toList();
    }

    public void delete(Integer id) throws SupplierNotFoundException, UserNotFoundException {
        Supplier supplier = supplierRepository.findById(id)
                .orElseThrow(() -> new SupplierNotFoundException("Supplier not found"));
        User updaterUser = userRepository.findById(authenticationService.getUserIdFromToken())
                .orElseThrow(() -> new UserNotFoundException("User not found"));

        supplier.setDeleted(true);
        supplier.setUpdatedBy(updaterUser);
        supplier.getAddress().setDeleted(true);
        supplier.getAddress().setUpdatedBy(updaterUser);
        supplier.getPaymentConditions().forEach(paymentCondition -> {
            paymentCondition.setDeleted(true);
            paymentCondition.setUpdatedBy(updaterUser);
        });
        supplierRepository.save(supplier);
    }

    public SupplierResponse update(Integer id, SupplierRequest request) throws SupplierNotFoundException, UserNotFoundException, CityNotFoundException, CuitAlreadyInUseException, EmailAlreadyInUseException, NameAlreadyInUseException {
        Supplier supplier = supplierRepository.findById(id)
                .orElseThrow(() -> new SupplierNotFoundException("Supplier not found"));

        User updaterUser = userRepository.findById(authenticationService.getUserIdFromToken())
                .orElseThrow(() -> new UserNotFoundException("User not found"));

        if (!Objects.equals(supplier.getCuit(), request.getCuit()) &&
            supplierRepository.findByCuit(request.getCuit()).isPresent()) {
            throw new CuitAlreadyInUseException("Cuit already in use");
        }

        if (!Objects.equals(supplier.getEmail(), request.getEmail()) &&
            supplierRepository.findByEmail(request.getEmail()).isPresent()) {
            throw new EmailAlreadyInUseException("Email already in use");
        }

        if (!Objects.equals(supplier.getName(), request.getName()) &&
            supplierRepository.findByName(request.getName()).isPresent()) {
            throw new NameAlreadyInUseException("Name already in use");
        }

        Integer supplierAddressId = supplier.getAddress().getId();
        addressService.update(supplierAddressId, request.getAddress());

        supplier.getPaymentConditions().forEach(paymentCondition -> {
            paymentCondition.setDeleted(true);
            paymentCondition.setUpdatedBy(updaterUser);
        });

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
                            .createdBy(updaterUser)
                            .build()
            );
        });

        supplier.setCuit(request.getCuit());
        supplier.setEmail(request.getEmail());
        supplier.setName(request.getName());
        supplier.setPhone(request.getPhone());
        supplier.setPaymentConditions(paymentConditions);
        supplier.setUpdatedBy(updaterUser);

        Supplier savedSupplier = supplierRepository.save(supplier);

        return new SupplierResponse(savedSupplier);
    }

    public PurchaseResponse createPurchase(Integer supplierId, PurchaseRequest request) throws UserNotFoundException, SupplierNotFoundException, PaymentConditionNotFoundException {
        Supplier supplier = supplierRepository.findById(supplierId)
                .orElseThrow(() -> new SupplierNotFoundException("Supplier not found"));

        PaymentCondition paymentCondition = supplier.getPaymentConditions().stream()
                .filter(pc -> pc.getId().equals(request.getPaymentConditionId()))
                .findFirst()
                .orElseThrow(() -> new PaymentConditionNotFoundException("Payment condition not found"));

        User creatorUser = userRepository.findById(authenticationService.getUserIdFromToken())
                .orElseThrow(() -> new UserNotFoundException("User not found"));

        Purchase purchase = Purchase.builder()
                            .purchaseDate(request.getPurchaseDate())
                            .total(request.getTotal())
                            .observation(request.getObservation())
                            .supplier(supplier)
                            .paymentCondition(paymentCondition)
                            .createdBy(creatorUser)
                            .build();

        Purchase savedPurchase = purchaseRepository.save(purchase);

        return new PurchaseResponse(savedPurchase);
    }

    public PurchaseResponse getPurchase(Integer supplierId, Integer id) throws PurchaseNotFoundException, SupplierNotFoundException {
        Supplier supplier = supplierRepository.findById(supplierId)
                .orElseThrow(() -> new SupplierNotFoundException("Supplier not found"));

        Purchase purchase = supplier.getPurchases()
                .stream()
                .filter(p -> p.getId().equals(id))
                .findFirst()
                .orElseThrow(() -> new PurchaseNotFoundException("Purchase not found"));

        return new PurchaseResponse(purchase);
    }

    public List<PurchaseResponse> getAllPurchases(Integer supplierId) throws SupplierNotFoundException {
        Supplier supplier = supplierRepository.findById(supplierId)
                .orElseThrow(() -> new SupplierNotFoundException("Supplier not found"));

        return supplier.getPurchases().stream()
                .map(PurchaseResponse::new)
                .toList();
    }

    public void deletePurchase(Integer supplierId, Integer id) throws UserNotFoundException, SupplierNotFoundException, PurchaseNotFoundException {
        User updaterUser = userRepository.findById(authenticationService.getUserIdFromToken())
                .orElseThrow(() -> new UserNotFoundException("User not found"));

        Supplier supplier = supplierRepository.findById(supplierId)
                .orElseThrow(() -> new SupplierNotFoundException("Supplier not found"));

        Purchase purchase = supplier.getPurchases()
                .stream()
                .filter(p -> p.getId().equals(id))
                .findFirst()
                .orElseThrow(() -> new PurchaseNotFoundException("Purchase not found"));

        purchase.setDeleted(true);
        purchase.setUpdatedBy(updaterUser);
        if(purchase.getPurchaseRating() != null) {
            purchase.getPurchaseRating().setDeleted(true);
            purchase.getPurchaseRating().setUpdatedBy(updaterUser);
        }

        purchaseRepository.save(purchase);
    }

    public PurchaseResponse updatePurchase(Integer supplierId, Integer id, PurchaseRequest request) throws UserNotFoundException, SupplierNotFoundException, PurchaseNotFoundException, PaymentConditionNotFoundException {
        User updaterUser = userRepository.findById(authenticationService.getUserIdFromToken())
                .orElseThrow(() -> new UserNotFoundException("User not found"));

        Supplier supplier = supplierRepository.findById(supplierId)
                .orElseThrow(() -> new SupplierNotFoundException("Supplier not found"));

        Purchase purchase = supplier.getPurchases()
                .stream()
                .filter(p -> p.getId().equals(id))
                .findFirst()
                .orElseThrow(() -> new PurchaseNotFoundException("Purchase not found"));

        PaymentCondition paymentCondition = supplier.getPaymentConditions().stream()
                .filter(pc -> pc.getId().equals(request.getPaymentConditionId()))
                .findFirst()
                .orElseThrow(() -> new PaymentConditionNotFoundException("Payment condition not found"));

        purchase.setPurchaseDate(request.getPurchaseDate());
        purchase.setTotal(request.getTotal());
        purchase.setObservation(request.getObservation());
        purchase.setPaymentCondition(paymentCondition);
        purchase.setUpdatedBy(updaterUser);

        Purchase savedPurchase = purchaseRepository.save(purchase);

        return new PurchaseResponse(savedPurchase);
    }
}
