package be.kdg.integration4.service;

import be.kdg.integration4.domain.TestStatus;
import be.kdg.integration4.domain.TestType;

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
