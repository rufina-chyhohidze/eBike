package be.kdg.integration4.service.dtos;

import be.kdg.integration4.domain.profile.Customer;

//TODO Rename for better readability
public record CustomerDto(
        Customer customer,
        String password
) {
}
