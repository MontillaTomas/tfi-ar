package com.example.tfi_ar.service;

import com.example.tfi_ar.dto.ClientInteractionRequest;
import com.example.tfi_ar.dto.ClientInteractionResponse;
import com.example.tfi_ar.dto.ClientRequest;
import com.example.tfi_ar.dto.ClientResponse;
import com.example.tfi_ar.exception.*;
import com.example.tfi_ar.model.Address;
import com.example.tfi_ar.model.Client;
import com.example.tfi_ar.model.ClientInteraction;
import com.example.tfi_ar.model.User;
import com.example.tfi_ar.repository.ClientInteractionRepository;
import com.example.tfi_ar.repository.ClientRepository;
import com.example.tfi_ar.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ClientService {
    private final ClientRepository clientRepository;
    private final ClientInteractionRepository clientInteractionRepository;
    private final UserRepository userRepository;
    private final AuthenticationService authenticationService;
    private final AddressService addressService;

    public ClientResponse create(ClientRequest request) throws UserNotFoundException, CityNotFoundException, EmailAlreadyInUseException, NameAlreadyInUseException {
        if(clientRepository.findByEmail(request.getEmail()).isPresent()) {
            throw new EmailAlreadyInUseException("Email already in use");
        }

        if(clientRepository.findByName(request.getName()).isPresent()) {
            throw new NameAlreadyInUseException("Name already in use");
        }

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
        client.getAddress().setDeleted(true);
        client.getAddress().setUpdatedBy(updateUser);
        clientRepository.save(client);
    }

    public ClientResponse update(Integer id, ClientRequest request) throws ClientNotFoundException, UserNotFoundException, CityNotFoundException, NameAlreadyInUseException, EmailAlreadyInUseException {
        Client client = clientRepository.findById(id)
                .orElseThrow(() -> new ClientNotFoundException("Client not found"));

        User updateUser = userRepository.findById(authenticationService.getUserIdFromToken())
                .orElseThrow(() -> new UserNotFoundException("User not found"));

        if( request.getName() != null &&
            !client.getName().equals(request.getName()) &&
            clientRepository.findByName(request.getName()).isPresent()) {
            throw new NameAlreadyInUseException("Name already in use");
        }

        if( request.getEmail() != null &&
            !client.getEmail().equals(request.getEmail()) &&
            clientRepository.findByEmail(request.getEmail()).isPresent()) {
            throw new EmailAlreadyInUseException("Email already in use");
        }

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

    public ClientInteractionResponse addInteraction(Integer id, ClientInteractionRequest request) throws UserNotFoundException {
        Client client = clientRepository.findById(id)
                .orElseThrow(() -> new ClientNotFoundException("Client not found"));

        User creatorUser = userRepository.findById(authenticationService.getUserIdFromToken())
                .orElseThrow(() -> new UserNotFoundException("User not found"));

        ClientInteraction interaction = ClientInteraction.builder()
                .date(request.getDate())
                .details(request.getDetails())
                .client(client)
                .createdBy(creatorUser)
                .build();

        ClientInteraction savedInteraction = clientInteractionRepository.save(interaction);

        return new ClientInteractionResponse(savedInteraction);
    }

    public void deleteInteraction(Integer clientId, Integer interactionId) throws ClientInteractionNotFoundException, UserNotFoundException {
        Client client = clientRepository.findById(clientId)
                .orElseThrow(() -> new ClientNotFoundException("Client not found"));

        ClientInteraction interaction = client.getInteractions().stream()
                .filter(i -> i.getId().equals(interactionId))
                .findFirst()
                .orElseThrow(() -> new ClientInteractionNotFoundException("Interaction not found"));

        User updateUser = userRepository.findById(authenticationService.getUserIdFromToken())
                .orElseThrow(() -> new UserNotFoundException("User not found"));

        interaction.setUpdatedBy(updateUser);
        interaction.setDeleted(true);
        clientInteractionRepository.save(interaction);
    }
}
