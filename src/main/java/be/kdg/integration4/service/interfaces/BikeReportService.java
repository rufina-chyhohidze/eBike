package be.kdg.integration4.service.interfaces;

import be.kdg.integration4.domain.enums.InspectionCondition;
import be.kdg.integration4.domain.profile.Technician;
import be.kdg.integration4.domain.report.BikeReport;
import be.kdg.integration4.domain.report.TestLine;
import be.kdg.integration4.domain.enums.TestType;
import be.kdg.integration4.service.dtos.*;
import jakarta.validation.constraints.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

public interface BikeReportService {

    BikeReport getById(Long id);

    BikeReport findByIdWithTestlinesAndBike(Long id);

    List<BikeReport> getAll();

    BikeReport save(Long id, String bike, String reportDate, Integer score, String technician, String customer);

    void delete(Long id);

    BikeReport save(Long testbenchNumber, TestType testType, String emailBikeOwner, String chassisNumber);

    BikeReport save(Long testbenchNumber, TestType testType, String emailBikeOwner, String chassisNumber, Map<String, InspectionCondition> inspection);

    BikeReport save(Long testbenchNumber, TestType testType, String emailBikeOwner, String chassisNumber, Map<String, InspectionCondition> inspection, Map<String, InspectionCondition> functionalTest);

    BikeReport update(Long id, String chassisNumber, LocalDate reportDate, Integer score, String technician, String customer, List<TestLine> testLines, Long benchId);

    List<BikeReport> getByCustomerId(Long customerId);

    List<String> getFrameNumbersByCustomerId(Long customerId);
    List<BikeReport> getAllReportsWithDetails();
    //List<BikeReport> searchReports(String frameNumber, String customerName);

    OverviewTestDTO calculateOverviewTest(Long reportId);

    NominalLoadTestDTO calculateNominalLoadTest(Long reportId);

    BatteryTestDTO calculateBatteryTest(Long reportId);

    BearingHealthDTO calculateBearingHealth(Long reportId, Technician user);

    FullTestReportDTO getFullTestReport(Long reportId);

}
