package com.seek.client_management.mapper;


import com.seek.client_management.model.ClientDto;
import com.seek.client_management.model.entity.ClientEntity;
import org.springframework.stereotype.Component;

@Component
public class ClientMapper {

    public ClientDto toDto(ClientEntity client) {
        ClientDto dto = new ClientDto();
        dto.setId(client.getId());
        dto.setFirstName(client.getFirstName());
        dto.setLastName(client.getLastName());
        dto.setAge(client.getAge());
        dto.setDateOfBirth(client.getDateOfBirth());
        return dto;
    }

    public ClientEntity toEntity(ClientDto dto) {
        ClientEntity client = new ClientEntity();
        client.setId(dto.getId());
        client.setFirstName(dto.getFirstName());
        client.setLastName(dto.getLastName());
        client.setAge(dto.getAge());
        client.setDateOfBirth(dto.getDateOfBirth());
        return client;
    }
}