package be.kdg.integration4.controller.api.dtos;

import jakarta.validation.constraints.*;

public record CustomerRegistrationDto(
        @NotNull @NotBlank
        String name,
        @NotNull @Email
        String email,
        @NotNull
//        @Size(min = 8)
        String password,
        @NotBlank(message = "Phone number is required")
//        @Pattern(
//                regexp = "^(\\+?[0-9]{1,3}[-.\\s]?)?[0-9]{2,4}[-.\\s]?[0-9]{3,4}[-.\\s]?[0-9]{3,4}$",
//                message = "Invalid phone number format"
//        )
        String phoneNumber
) {
}
