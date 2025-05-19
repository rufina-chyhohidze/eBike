package be.kdg.integration4.controller.api.dtos;


import java.time.LocalDate;

public record ShortBikeReportDto(
        Long reportId,
        LocalDate reportDate,
        Long customerId,
        String frameNumber,
        String engineType,
        Long benchId
) {
}
