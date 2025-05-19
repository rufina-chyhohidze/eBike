package be.kdg.integration4.controller.api.dtos;

public record UpdateRequest(
        String phoneNumber,
        String password
) {
}
