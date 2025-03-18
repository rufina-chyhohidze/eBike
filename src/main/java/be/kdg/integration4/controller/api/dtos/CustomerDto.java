package be.kdg.integration4.controller.api.dtos;


public record CustomerDto(
        Integer id,
        String name,
        String email,
        String phoneNumber
) {
}
