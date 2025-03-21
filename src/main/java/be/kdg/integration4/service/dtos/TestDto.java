package be.kdg.integration4.service.dtos;

import be.kdg.integration4.domain.enums.TestStatus;
import be.kdg.integration4.domain.enums.TestType;

import java.time.LocalDateTime;

public record TestDto(
        String id,
        LocalDateTime expiryDate,
        TestStatus state,
        TestType type,
        int batteryCapacity,
        int maxSupport,
        int enginePowerMax,
        int enginePowerNominal,
        int engineTorque
) {
}
