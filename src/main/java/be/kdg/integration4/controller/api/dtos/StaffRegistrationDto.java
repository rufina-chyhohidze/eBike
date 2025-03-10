package be.kdg.integration4.controller.api.dtos;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record StaffRegistrationDto(
        @NotNull @NotBlank
        String name,
        @NotNull @Email
        String email,
        @NotNull
//        @Size(min = 8)
        String password,
        @NotNull
        String userRole,
        @NotNull
        Long workshopId
) {

}
