package be.kdg.integration4.service.dtos;

import be.kdg.integration4.domain.profile.Customer;

public record CustomerAndPasswordServiceDto(
        Customer customer,
        String password
) {
}
