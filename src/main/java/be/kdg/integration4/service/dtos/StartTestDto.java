package be.kdg.integration4.service.dtos;

public record StartTestDto(
        String type,
        int batteryCapacity,
        int maxSupport,
        int enginePowerMax,
        int enginePowerNominal,
        int engineTorque
) {
}
