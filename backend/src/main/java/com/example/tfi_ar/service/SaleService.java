package com.example.tfi_ar.service;

import com.example.tfi_ar.dto.SaleRequest;
import com.example.tfi_ar.dto.SaleResponse;
import com.example.tfi_ar.exception.SaleNotFoundException;
import com.example.tfi_ar.exception.UserNotFoundException;
import com.example.tfi_ar.model.*;
import com.example.tfi_ar.repository.SaleRepository;
import com.example.tfi_ar.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class SaleService {
    private final SaleRepository saleRepository;
    private final UserRepository userRepository;
    private final AuthenticationService authenticationService;

    public SaleResponse create(SaleRequest request) throws UserNotFoundException {
        User creatorUser = userRepository.findById(authenticationService.getUserIdFromToken())
                .orElseThrow(() -> new UserNotFoundException("User not found"));

        // Create invoice details
        List<InvoiceDetail> details = new ArrayList<>();
        request.getInvoice().getDetails().forEach(detail -> {
            details.add(
                    InvoiceDetail.builder()
                            .description(detail.getDescription())
                            .quantity(detail.getQuantity())
                            .unitPrice(detail.getUnitPrice())
                            .createdBy(creatorUser)
                            .build()
            );
        });

        // Create invoice
        Invoice invoice = Invoice.builder()
                            .issueDate(request.getInvoice().getIssueDate())
                            .dueDate(request.getInvoice().getDueDate())
                            .state(InvoiceState.PENDING)
                            .paymentMethod(request.getInvoice().getPaymentMethod())
                            .observation(request.getInvoice().getObservation())
                            .state(request.getInvoice().getState())
                            .details(details)
                            .createdBy(creatorUser)
                            .build();

        details.forEach(detail -> detail.setInvoice(invoice));

        Sale sale = Sale.builder()
                .saleDate(request.getSaleDate())
                .observation(request.getObservation())
                .invoice(invoice)
                .createdBy(creatorUser)
                .build();

        invoice.setSale(sale);

        Sale savedSale = saleRepository.save(sale);

        return new SaleResponse(savedSale);
    }

    public List<SaleResponse> getAll() {
        return saleRepository.findAll().stream()
                .map(SaleResponse::new)
                .toList();
    }

    public SaleResponse get(Integer id) throws SaleNotFoundException {
        Sale sale = saleRepository.findById(id).orElseThrow(() -> new SaleNotFoundException("Sale not found"));
        return new SaleResponse(sale);
    }

    private void deleteInvoiceDetails(Invoice invoice, User updaterUser) {
        invoice.getDetails().forEach(detail -> {
            detail.setDeleted(true);
            detail.setUpdatedBy(updaterUser);
        });
    }

    public void delete(Integer id) throws SaleNotFoundException, UserNotFoundException {
        Sale sale = saleRepository.findById(id).orElseThrow(() -> new SaleNotFoundException("Sale not found"));

        User updaterUser = userRepository.findById(authenticationService.getUserIdFromToken())
                .orElseThrow(() -> new UserNotFoundException("User not found"));

        // Set deleted to true to all invoice details

        deleteInvoiceDetails(sale.getInvoice(), updaterUser);

        // Set deleted to true to invoice

        sale.getInvoice().setDeleted(true);
        sale.getInvoice().setUpdatedBy(updaterUser);

        // Set deleted to true to sale

        sale.setUpdatedBy(updaterUser);
        sale.setDeleted(true);

        saleRepository.save(sale);
    }

    public SaleResponse update(Integer id, SaleRequest request) throws SaleNotFoundException, UserNotFoundException {
        Sale sale = saleRepository.findById(id).orElseThrow(() -> new SaleNotFoundException("Sale not found"));

        User updaterUser = userRepository.findById(authenticationService.getUserIdFromToken())
                .orElseThrow(() -> new UserNotFoundException("User not found"));

        // Delete all invoice details

        deleteInvoiceDetails(sale.getInvoice(), updaterUser);

        // Create new invoice details

        List<InvoiceDetail> details = new ArrayList<>();
        request.getInvoice().getDetails().forEach(detail -> {
            details.add(
                    InvoiceDetail.builder()
                            .description(detail.getDescription())
                            .quantity(detail.getQuantity())
                            .unitPrice(detail.getUnitPrice())
                            .invoice(sale.getInvoice())
                            .createdBy(updaterUser)
                            .build()
            );
        });

        // Update invoice

        sale.getInvoice().setIssueDate(request.getInvoice().getIssueDate());
        sale.getInvoice().setDueDate(request.getInvoice().getDueDate());
        sale.getInvoice().setPaymentMethod(request.getInvoice().getPaymentMethod());
        sale.getInvoice().setObservation(request.getInvoice().getObservation());
        sale.getInvoice().setState(request.getInvoice().getState());
        sale.getInvoice().setDetails(details);
        sale.getInvoice().setUpdatedBy(updaterUser);

        // Update sale

        sale.setSaleDate(request.getSaleDate());
        sale.setObservation(request.getObservation());
        sale.setUpdatedBy(updaterUser);

        return new SaleResponse(saleRepository.save(sale));
    }
}
