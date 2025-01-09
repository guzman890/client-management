package com.seek.client_management.service;

import com.seek.client_management.model.ClientDto;
import com.seek.client_management.model.MetricsDto;

import java.util.List;

public interface ClientService {
    ClientDto createClient(ClientDto client);
    List<ClientDto> getAllClients();
    MetricsDto getClientMetrics();
    ClientDto getClientById(Integer id);
    ClientDto updateClient(Integer id, ClientDto updatedClient);
    void deleteClient(Integer id);
    List<ClientDto> getClientsWithEstimatedDate(String event);
}