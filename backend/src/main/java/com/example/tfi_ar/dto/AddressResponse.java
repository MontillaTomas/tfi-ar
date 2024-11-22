package com.example.tfi_ar.dto;

import com.example.tfi_ar.model.Address;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AddressResponse {
    private Integer id;
    private String street;
    private String number;
    private String floor;
    private String apartment;
    private String postalCode;
    private String city;
    private String state;
    private String country;
    private String observations;

    public AddressResponse(Address address) {
        this.id = address.getId();
        this.street = address.getStreet();
        this.number = address.getNumber();
        this.floor = address.getFloor();
        this.apartment = address.getApartment();
        this.postalCode = address.getPostalCode();
        this.city = address.getCity().getName();
        this.state = address.getCity().getState().getName();
        this.country = address.getCity().getState().getCountry().getName();
        this.observations = address.getObservations();
    }
}
