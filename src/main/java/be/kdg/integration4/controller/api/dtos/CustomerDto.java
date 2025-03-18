package be.kdg.integration4.controller.api.dtos;

import org.mapstruct.Mapping;

public record CustomerDto(
        String name,
        String email,
        String phoneNumber
) {
}
