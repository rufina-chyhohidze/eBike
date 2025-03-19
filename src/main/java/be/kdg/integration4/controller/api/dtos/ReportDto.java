package be.kdg.integration4.controller.api.dtos;

import java.time.LocalDate;
import java.util.List;

public record ReportDto(
        Long id,
        Long bikeId,
        LocalDate reportDate,
        Integer score,
        Long technicianId,
        Long customerId,
        Long testBenchId,
        List<TestLineDto> testLineDtos

) {
}
