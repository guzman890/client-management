package com.seek.client_management.controller;

import com.seek.client_management.api.ClientsApi;
import com.seek.client_management.model.ClientDto;
import com.seek.client_management.model.MetricsDto;
import com.seek.client_management.service.ClientService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;


import java.util.List;

@RestController
public class ClientController implements ClientsApi {

    @Autowired
    private ClientService clientService;

    @Override
    public ResponseEntity<ClientDto> createClient(
            @Valid @RequestBody ClientDto clientDto
    ) {
        return ResponseEntity.ok(clientService.createClient(clientDto));
    }

    @Override
    public ResponseEntity<Void> deleteClient(Integer id) {
        clientService.deleteClient(id);
        return ResponseEntity.noContent().build();
    }

    @Override
    public ResponseEntity<List<ClientDto>> getAllClients() {
        return ResponseEntity.ok(clientService.getAllClients());
    }

    @Override
    public ResponseEntity<ClientDto> getClientById(Integer id) {
        return ResponseEntity.ok(clientService.getClientById(id));
    }

    @Override
    public ResponseEntity<MetricsDto> getClientMetrics() {
        return ResponseEntity.ok(clientService.getClientMetrics());
    }

    @Override
    public ResponseEntity<List<ClientDto>> getClientsWithEstimatedDate(String event) {
        return ResponseEntity.ok(clientService.getClientsWithEstimatedDate(event));
    }

    @Override
    public ResponseEntity<Void> updateClient(Integer id, @Valid @RequestBody ClientDto client) {
        clientService.updateClient(id, client);
        return ResponseEntity.noContent().build();
    }
}