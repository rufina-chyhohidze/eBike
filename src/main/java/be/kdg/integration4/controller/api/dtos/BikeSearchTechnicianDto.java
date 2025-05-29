package be.kdg.integration4.controller.api.dtos;

public record BikeSearchTechnicianDto(
        String frameNumber,
        String type,
        String brand,
        String customerName
) {
}
