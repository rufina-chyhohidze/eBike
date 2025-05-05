package be.kdg.integration4.controller.api.dtos;

import be.kdg.integration4.domain.enums.BikeSize;
import jakarta.validation.constraints.*;

import java.time.LocalDate;

public record BikeDto(
        @NotNull(message = "Bike owner ID is required")
        Long bikeOwnerId,

        @NotBlank(message = "Frame number is required")
        @Size(min = 5, max = 20, message = "Frame number must be between 5 and 20 characters")
        String frameNumber,

        @NotBlank(message = "Type is required")
        String type,

        @NotBlank(message = "Brand is required")
        String brand,

//        @NotNull(message = "Registration date is required")
        @PastOrPresent(message = "Registration date must be in the past or present")
        LocalDate registrationDate,

        @NotNull(message = "Production date is required")
        @Past(message = "Production date must be in the past")
        LocalDate productionDate,

        @NotNull(message = "Bike size is required")
        BikeSize bikeSize,

        @NotNull(message = "Mileage is required")
        @Min(value = 0, message = "Mileage must be a positive number")
        Integer milleage,

        @NotBlank(message = "Gear type is required")
        String gearType,

        @NotBlank(message = "Engine type is required")
        String engineType,

        @NotBlank(message = "Powertrain is required")
        String powertrain,

        @NotNull(message = "Accumulator capacity is required")
        @Min(value = 0, message = "Accumulator capacity must be a positive number")
        Integer accCapacity,

        @NotNull(message = "Maximum support is required")
        @Min(value = 0, message = "Maximum support must be a positive number")
        Integer maxSupport,

        @NotNull(message = "Engine power max is required")
        @Min(value = 0, message = "Engine power max must be a positive number")
        Integer enginePowerMax,

        @NotNull(message = "Engine power nominal is required")
        @Min(value = 0, message = "Engine power nominal must be a positive number")
        Integer enginePowerNominal,

        @NotNull(message = "Engine torque is required")
        @Min(value = 0, message = "Engine torque must be a positive number")
        Integer engineTorque
) {
}
