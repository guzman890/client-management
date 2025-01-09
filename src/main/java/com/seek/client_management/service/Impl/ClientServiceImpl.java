package com.seek.client_management.service.Impl;

import com.seek.client_management.business.ClientValidationService;
import com.seek.client_management.exception.NotFoundException;
import com.seek.client_management.mapper.ClientMapper;
import com.seek.client_management.model.ClientDto;
import com.seek.client_management.model.MetricsDto;
import com.seek.client_management.model.entity.ClientEntity;
import com.seek.client_management.repository.ClientRepository;
import com.seek.client_management.service.ClientService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

import org.apache.commons.math3.stat.descriptive.DescriptiveStatistics;

@Service
@Transactional
public class ClientServiceImpl implements ClientService {

    private static final Logger logger = LoggerFactory.getLogger(ClientServiceImpl.class);

    @Autowired
    private ClientRepository clientRepository;

    @Autowired
    private ClientMapper clientMapper;

    @Autowired
    private ClientValidationService clientValidationService;

    @Override
    public ClientDto createClient(ClientDto clientDto) {
        logger.info("Creating client: {}", clientDto.getFirstName());
        clientValidationService.validateClient(clientDto);
        return clientMapper.toDto(clientRepository.save(clientMapper.toEntity(clientDto)));
    }

    @Override
    public List<ClientDto> getAllClients() {
        logger.info("Getting all clients");
        return clientRepository.findAll().stream()
                .map(clientMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public MetricsDto getClientMetrics() {
        logger.info("Calculating client metrics");
        DescriptiveStatistics stats = new DescriptiveStatistics();
        clientRepository.findAll().forEach(c -> stats.addValue(c.getAge()));

        MetricsDto metrics = new MetricsDto();
        metrics.setAverageAge(BigDecimal.valueOf(stats.getMean()));
        metrics.setStandardDeviationAge(BigDecimal.valueOf(stats.getStandardDeviation()));
        return metrics;
    }

    @Override
    public ClientDto getClientById(Integer id) {
        logger.info("Getting client with ID: {}", id);
        return clientRepository.findById(id)
                .map(clientMapper::toDto)
                .orElseThrow(() -> new NotFoundException("Client not found with ID: " + id));
    }

    @Override
    public ClientDto updateClient(Integer id, ClientDto clientDto) {
        logger.info("Updating client with ID: {}", id);
        clientValidationService.validateClient(clientDto);
        return clientRepository.findById(id)
                .map( existingClient -> {
                    existingClient.setFirstName(clientDto.getFirstName());
                    existingClient.setLastName(clientDto.getLastName());
                    existingClient.setAge(clientDto.getAge());
                    existingClient.setDateOfBirth(clientDto.getDateOfBirth());
                    return clientRepository.save(existingClient);
                })
                .map(clientMapper::toDto)
                .orElseThrow(() -> new NotFoundException("Client not found with ID: " + id));
    }

    @Override
    public void deleteClient(Integer id) {
        logger.info("Deleting client with ID: {}", id);
        clientRepository.findById(id).ifPresentOrElse(
                clientRepository::delete,
                () -> { throw new NotFoundException("Client not found with ID: " + id); }
        );
    }

    @Override
    public List<ClientDto> getClientsWithEstimatedDate(String event) {
        logger.info("Listing clients with estimated date for event: {}", event);
        LocalDate eventDate = LocalDate.parse(event);
        List<ClientEntity> clients = clientRepository.findAll();
        return clients.stream().map(client -> {
            ClientDto copiedClient = clientMapper.toDto(client);
            int newAge = eventDate.getYear() - client.getDateOfBirth().getYear();
            if (eventDate.getDayOfYear() < client.getDateOfBirth().getDayOfYear()) {
                newAge--;
            }
            copiedClient.setAge(newAge);
            return copiedClient;
        }).collect(Collectors.toList());
    }
}