package com.seek.client_management.controller;

import com.seek.client_management.model.ClientDto;
import com.seek.client_management.model.MetricsDto;
import com.seek.client_management.service.ClientService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;

import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ClientControllerTest {

    @Mock
    private ClientService clientService;

    @InjectMocks
    private ClientController clientController;

    private ClientDto clientDto;

    @BeforeEach
    void setUp() {
        clientDto = new ClientDto();
        clientDto.setId(1);
        clientDto.setFirstName("Test Client");
    }

    @Test
    void testCreateClient() {
        when(clientService.createClient(any(ClientDto.class))).thenReturn(clientDto);

        ResponseEntity<ClientDto> response = clientController.createClient(clientDto);

        assertEquals(200, response.getStatusCodeValue());
        assertEquals(clientDto, response.getBody());
    }

    @Test
    void testDeleteClient() {
        doNothing().when(clientService).deleteClient(1);

        ResponseEntity<Void> response = clientController.deleteClient(1);

        assertEquals(204, response.getStatusCodeValue());
        verify(clientService, times(1)).deleteClient(1);
    }

    @Test
    void testGetAllClients() {
        when(clientService.getAllClients()).thenReturn(Collections.singletonList(clientDto));

        ResponseEntity<List<ClientDto>> response = clientController.getAllClients();

        assertEquals(200, response.getStatusCodeValue());
        assertEquals(1, response.getBody().size());
        assertEquals(clientDto, response.getBody().get(0));
    }

    @Test
    void testGetClientById() {
        when(clientService.getClientById(1)).thenReturn(clientDto);

        ResponseEntity<ClientDto> response = clientController.getClientById(1);

        assertEquals(200, response.getStatusCodeValue());
        assertEquals(clientDto, response.getBody());
    }

    @Test
    void testGetClientMetrics() {
        MetricsDto metricsDto = new MetricsDto();
        when(clientService.getClientMetrics()).thenReturn(metricsDto);

        ResponseEntity<MetricsDto> response = clientController.getClientMetrics();

        assertEquals(200, response.getStatusCodeValue());
        assertEquals(metricsDto, response.getBody());
    }

    @Test
    void testGetClientsWithEstimatedDate() {
        when(clientService.getClientsWithEstimatedDate("event")).thenReturn(Collections.singletonList(clientDto));

        ResponseEntity<List<ClientDto>> response = clientController.getClientsWithEstimatedDate("event");

        assertEquals(200, response.getStatusCodeValue());
        assertEquals(1, response.getBody().size());
        assertEquals(clientDto, response.getBody().get(0));
    }

    @Test
    void testUpdateClient() {
        when(clientService.updateClient(anyInt(), any(ClientDto.class))).thenReturn(clientDto);

        ResponseEntity<Void> response = clientController.updateClient(1, clientDto);

        assertEquals(204, response.getStatusCodeValue());
        verify(clientService, times(1)).updateClient(1, clientDto);
    }
}