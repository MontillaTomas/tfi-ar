package com.example.tfi_ar.service;

import com.example.tfi_ar.dto.ClientRequest;
import com.example.tfi_ar.dto.ClientResponse;
import com.example.tfi_ar.exception.CityNotFoundException;
import com.example.tfi_ar.exception.ClientNotFoundException;
import com.example.tfi_ar.exception.UserNotFoundException;
import com.example.tfi_ar.model.Address;
import com.example.tfi_ar.model.Client;
import com.example.tfi_ar.model.User;
import com.example.tfi_ar.repository.ClientRepository;
import com.example.tfi_ar.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ClientService {
    private final ClientRepository clientRepository;
    private final UserRepository userRepository;
    private final AuthenticationService authenticationService;
    private final AddressService addressService;

    public ClientResponse create(ClientRequest request) throws UserNotFoundException, CityNotFoundException {
        User creatorUser = userRepository.findById(authenticationService.getUserIdFromToken())
                .orElseThrow(() -> new UserNotFoundException("User not found"));

        Address address = addressService.create(request.getAddress());

        var client = Client.builder()
                .name(request.getName())
                .email(request.getEmail())
                .phone(request.getPhone())
                .contactName(request.getContactName())
                .address(address)
                .industry(request.getIndustry())
                .estimatedTransactionsNumber(request.getEstimatedTransactionsNumber())
                .technologiesUsed(request.getTechnologiesUsed())
                .remarks(request.getRemarks())
                .createdBy(creatorUser)
                .build();

        Client savedClient = clientRepository.save(client);

        return new ClientResponse(savedClient);
    }

    public ClientResponse get(Integer id) throws ClientNotFoundException {
        Client client = clientRepository.findById(id)
                .orElseThrow(() -> new ClientNotFoundException("Client not found"));

        return new ClientResponse(client);
    }

    public List<ClientResponse> getAll() {
        return clientRepository.findAll().stream()
                .map(ClientResponse::new)
                .toList();
    }

    public void delete(Integer id) throws ClientNotFoundException, UserNotFoundException {
        Client client = clientRepository.findById(id)
                .orElseThrow(() -> new ClientNotFoundException("Client not found"));

        User updateUser = userRepository.findById(authenticationService.getUserIdFromToken())
                .orElseThrow(() -> new UserNotFoundException("User not found"));

        client.setUpdatedBy(updateUser);
        client.setDeleted(true);
        clientRepository.save(client);
    }

    public ClientResponse update(Integer id, ClientRequest request) throws ClientNotFoundException, UserNotFoundException, CityNotFoundException {
        Client client = clientRepository.findById(id)
                .orElseThrow(() -> new ClientNotFoundException("Client not found"));

        User updateUser = userRepository.findById(authenticationService.getUserIdFromToken())
                .orElseThrow(() -> new UserNotFoundException("User not found"));

        addressService.update(client.getAddress().getId(), request.getAddress());

        if(request.getName() != null) {
            client.setName(request.getName());
        }
        if(request.getEmail() != null) {
            client.setEmail(request.getEmail());
        }
        if(request.getPhone() != null) {
            client.setPhone(request.getPhone());
        }
        if(request.getContactName() != null) {
            client.setContactName(request.getContactName());
        }
        if(request.getIndustry() != null) {
            client.setIndustry(request.getIndustry());
        }
        if(request.getEstimatedTransactionsNumber() != null) {
            client.setEstimatedTransactionsNumber(request.getEstimatedTransactionsNumber());
        }
        if(request.getTechnologiesUsed() != null) {
            client.setTechnologiesUsed(request.getTechnologiesUsed());
        }
        if(request.getRemarks() != null) {
            client.setRemarks(request.getRemarks());
        }
        client.setUpdatedBy(updateUser);

        Client updatedClient = clientRepository.save(client);

        return new ClientResponse(updatedClient);
    }

}
