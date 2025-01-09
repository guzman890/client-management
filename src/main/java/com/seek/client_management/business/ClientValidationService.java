package com.seek.client_management.business;

import com.seek.client_management.exception.ValidationException;
import com.seek.client_management.model.ClientDto;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service
public class ClientValidationService {

    public void validateClient(ClientDto clientDto) {
        if (clientDto.getFirstName() == null || clientDto.getFirstName().isEmpty()) {
            throw new ValidationException("First name must not be null or empty");
        }
        if (clientDto.getLastName() == null || clientDto.getLastName().isEmpty()) {
            throw new ValidationException("Last name must not be null or empty");
        }
        if (clientDto.getAge() == null || clientDto.getAge() <= 0) {
            throw new ValidationException("Age must be greater than 0");
        }
        if (clientDto.getDateOfBirth() == null) {
            throw new ValidationException("Date of birth must not be null");
        }
        if (clientDto.getDateOfBirth().isAfter(LocalDate.now())) {
            throw new ValidationException("Date of birth must not be a future date");
        }
    }
}
