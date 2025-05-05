package be.kdg.integration4.controller.api.dtos;

import be.kdg.integration4.domain.enums.BikeSize;

public record BikeModelOutputDto(
         Long id,

         String type,

         String brand,

         BikeSize bikeSize,

         String gearType,

         String engineType,

         String powertrain,

         double maxSupport,

         int enginePowerMax,

         int enginePowerNominal,

         int engineTorque

) {
}
