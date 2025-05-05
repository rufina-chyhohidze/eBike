package be.kdg.integration4.service.dtos;

import be.kdg.integration4.domain.enums.InspectionCondition;
import be.kdg.integration4.domain.enums.TestStatus;
import be.kdg.integration4.domain.enums.TestType;

import java.time.LocalDateTime;
import java.util.Map;

public record TestDto(
        String id,
        LocalDateTime expiryDate,
        TestStatus state,
        TestType type,
        int batteryCapacity,
        int maxSupport,
        int enginePowerMax,
        int enginePowerNominal,
        int engineTorque,
        Map<String, InspectionCondition> visualInspection,// Add visual inspection data as a Map of parts and their conditions
        Map<String, InspectionCondition> functionalTest// Add visual inspection data as a Map of parts and their conditions

) {
}
