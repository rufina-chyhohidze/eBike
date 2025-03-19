package be.kdg.integration4.service;

import be.kdg.integration4.domain.BikeReport;
import be.kdg.integration4.domain.TestLine;
import be.kdg.integration4.domain.TestType;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.function.ToDoubleFunction;

public interface BikeReportService {

    BikeReport findById(Long id);

    BikeReport findByIdWithTestlines(Long id);

    List<BikeReport> findAll();

    BikeReport save(Long id, String bike, String reportDate, Integer score, String technician, String customer);

    void delete(Long id);

    BikeReport save(Long testbenchNumber, TestType testType, String emailBikeOwner, String chassisNumber);

    BikeReport update(Long id, String chassisNumber, LocalDate reportDate, Integer score, String technician, String customer, List<TestLine> testLines, Long benchId);

    Map<String, Double> calculateAverages(List<TestLine> testLines);
}
