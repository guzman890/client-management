package com.seek.client_management.service.Impl;

import com.seek.client_management.business.ClientValidationService;
import com.seek.client_management.exception.NotFoundException;
import com.seek.client_management.mapper.ClientMapper;
import com.seek.client_management.model.ClientDto;
import com.seek.client_management.model.MetricsDto;
import com.seek.client_management.model.entity.ClientEntity;
import com.seek.client_management.repository.ClientRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ClientServiceImplTest {

    @Mock
    private ClientRepository clientRepository;

    @Mock
    private ClientMapper clientMapper;

    @Mock
    private ClientValidationService clientValidationService;

    @InjectMocks
    private ClientServiceImpl clientService;

    private ClientDto clientDto;
    private ClientEntity clientEntity;

    @BeforeEach
    void setUp() {
        clientDto = new ClientDto();
        clientDto.setId(1);
        clientDto.setFirstName("Test Client");

        clientEntity = new ClientEntity();
        clientEntity.setId(1);
        clientEntity.setFirstName("Test Client");
    }

    @Test
    void testCreateClient() {
        when(clientMapper.toEntity(any(ClientDto.class))).thenReturn(clientEntity);
        when(clientRepository.save(any(ClientEntity.class))).thenReturn(clientEntity);
        when(clientMapper.toDto(any(ClientEntity.class))).thenReturn(clientDto);

        ClientDto result = clientService.createClient(clientDto);

        assertEquals(clientDto, result);
        verify(clientValidationService, times(1)).validateClient(clientDto);
        verify(clientRepository, times(1)).save(clientEntity);
    }

    @Test
    void testGetAllClients() {
        when(clientRepository.findAll()).thenReturn(Collections.singletonList(clientEntity));
        when(clientMapper.toDto(any(ClientEntity.class))).thenReturn(clientDto);

        List<ClientDto> result = clientService.getAllClients();

        assertEquals(1, result.size());
        assertEquals(clientDto, result.get(0));
    }

    @Test
    void testGetClientById() {
        when(clientRepository.findById(1)).thenReturn(Optional.of(clientEntity));
        when(clientMapper.toDto(any(ClientEntity.class))).thenReturn(clientDto);

        ClientDto result = clientService.getClientById(1);

        assertEquals(clientDto, result);
    }

    @Test
    void testGetClientById_NotFound() {
        when(clientRepository.findById(1)).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> clientService.getClientById(1));
    }

    @Test
    void testUpdateClient() {
        when(clientRepository.findById(1)).thenReturn(Optional.of(clientEntity));
        when(clientRepository.save(any(ClientEntity.class))).thenReturn(clientEntity);
        when(clientMapper.toDto(any(ClientEntity.class))).thenReturn(clientDto);

        ClientDto result = clientService.updateClient(1, clientDto);

        assertEquals(clientDto, result);
        verify(clientValidationService, times(1)).validateClient(clientDto);
        verify(clientRepository, times(1)).save(clientEntity);
    }

    @Test
    void testDeleteClient() {
        when(clientRepository.findById(1)).thenReturn(Optional.of(clientEntity));
        doNothing().when(clientRepository).delete(clientEntity);

        clientService.deleteClient(1);

        verify(clientRepository, times(1)).delete(clientEntity);
    }

    @Test
    void testDeleteClient_NotFound() {
        when(clientRepository.findById(1)).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> clientService.deleteClient(1));
    }

    @Test
    void testGetClientMetrics() {
        when(clientRepository.findAll()).thenReturn(Collections.singletonList(clientEntity));
        clientEntity.setAge(30);

        MetricsDto result = clientService.getClientMetrics();

        assertEquals(BigDecimal.valueOf(30.0), result.getAverageAge());
        assertEquals(BigDecimal.valueOf(0.0), result.getStandardDeviationAge());
    }

    @Test
    void testGetClientsWithEstimatedDate() {
        when(clientRepository.findAll()).thenReturn(Collections.singletonList(clientEntity));
        when(clientMapper.toDto(any(ClientEntity.class))).thenReturn(clientDto);
        clientEntity.setDateOfBirth(LocalDate.of(1990, 1, 1));

        List<ClientDto> result = clientService.getClientsWithEstimatedDate("2023-01-01");

        assertEquals(1, result.size());
        assertEquals(33, result.get(0).getAge());
    }
}