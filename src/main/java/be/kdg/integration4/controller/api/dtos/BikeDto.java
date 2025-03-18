package be.kdg.integration4.controller.api.dtos;

import be.kdg.integration4.domain.BikeSize;

import java.time.LocalDate;

public record BikeDto(
        String frameNumber,
        String type,
        String brand,
        LocalDate registrationDate,
        LocalDate productionDate,
        BikeSize bikeSize,
        Integer milleage,
        String gearType,
        String engineType,
        String powertrain,
        Integer accCapacity,
        Integer maxSupport,
        Integer enginePowerMax,
        Integer enginePowerNominal,
        Integer engineTorque
) {
}
