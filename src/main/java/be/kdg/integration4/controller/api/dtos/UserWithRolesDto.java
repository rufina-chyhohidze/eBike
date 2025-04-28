package be.kdg.integration4.controller.api.dtos;

public record UserWithRolesDto (
        Long id,
        String name,
        String email,
        String role
) {
}
