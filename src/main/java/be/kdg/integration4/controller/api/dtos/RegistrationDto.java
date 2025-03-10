package be.kdg.integration4.controller.api.dtos;

public record RegistrationDto(
        String role,
        String name,
        String email,
        String password,
        String phoneNumber,
        Long workshopId
) {
}
