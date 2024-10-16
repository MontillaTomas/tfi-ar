package com.example.tfi_ar.service;

import com.example.tfi_ar.dto.AddressRequest;
import com.example.tfi_ar.exception.CityNotFoundException;
import com.example.tfi_ar.exception.UserNotFoundException;
import com.example.tfi_ar.model.Address;
import com.example.tfi_ar.model.City;
import com.example.tfi_ar.model.User;
import com.example.tfi_ar.repository.AddressRepository;
import com.example.tfi_ar.repository.CityRepository;
import com.example.tfi_ar.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AddressService {
    private final AddressRepository addressRepository;
    private final CityRepository cityRepository;
    private final UserRepository userRepository;
    private final AuthenticationService authenticationService;

    public Address create(AddressRequest addressRequest) throws CityNotFoundException, UserNotFoundException {
        City city = cityRepository.findById(addressRequest.getCityId())
                .orElseThrow(() -> new CityNotFoundException("City not found"));

        User creatorUser = userRepository.findById(authenticationService.getUserIdFromToken())
                .orElseThrow(() -> new UserNotFoundException("User not found"));

        var newAddress = Address.builder()
                .street(addressRequest.getStreet())
                .number(addressRequest.getNumber())
                .floor(addressRequest.getFloor())
                .apartment(addressRequest.getApartment())
                .postalCode(addressRequest.getPostalCode())
                .city(city)
                .observations(addressRequest.getObservations())
                .createdBy(creatorUser)
                .build();

        return addressRepository.save(newAddress);
    }

    public void update(Integer id, AddressRequest addressRequest) throws CityNotFoundException, UserNotFoundException {
        Address address = addressRepository.findById(id).orElseThrow();
        City city = cityRepository.findById(addressRequest.getCityId())
                .orElseThrow(() -> new CityNotFoundException("City not found"));

        if(addressRequest.getStreet() != null) address.setStreet(addressRequest.getStreet());
        if(addressRequest.getNumber() != null) address.setNumber(addressRequest.getNumber());
        if(addressRequest.getFloor() != null) address.setFloor(addressRequest.getFloor());
        if(addressRequest.getApartment() != null) address.setApartment(addressRequest.getApartment());
        if(addressRequest.getPostalCode() != null) address.setPostalCode(addressRequest.getPostalCode());
        if(addressRequest.getCityId() != null) address.setCity(city);
        if(addressRequest.getObservations() != null) address.setObservations(addressRequest.getObservations());

        User updateUser = userRepository.findById(authenticationService.getUserIdFromToken())
                .orElseThrow(() -> new UserNotFoundException("User not found"));

        address.setUpdatedBy(updateUser);

        addressRepository.save(address);
    }
}
