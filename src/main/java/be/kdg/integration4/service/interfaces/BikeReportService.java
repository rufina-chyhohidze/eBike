package be.kdg.integration4.service.interfaces;

import be.kdg.integration4.domain.report.BikeReport;
import be.kdg.integration4.domain.report.TestLine;
import be.kdg.integration4.domain.enums.TestType;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

public interface BikeReportService {

    BikeReport getById(Long id);

    BikeReport getByIdWithTestlines(Long id);

    List<BikeReport> getAll();

    BikeReport save(Long id, String bike, String reportDate, Integer score, String technician, String customer);

    void delete(Long id);

    BikeReport save(Long testbenchNumber, TestType testType, String emailBikeOwner, String chassisNumber);

    BikeReport update(Long id, String chassisNumber, LocalDate reportDate, Integer score, String technician, String customer, List<TestLine> testLines, Long benchId);

    Map<String, Double> calculateAverages(List<TestLine> testLines);

    List<BikeReport> getByCustomerId(Long customerId);

    List<String> getFrameNumbersByCustomerId(Long customerId);
    List<BikeReport> getAllWithDetails();
}
