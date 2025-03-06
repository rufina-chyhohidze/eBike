package be.kdg.integration4.controller.mvc.dtos;

public record RegistrationDto(
        String name,
        String email,
        String password,
        String userRole,
        String confirmPassword
) {
}
