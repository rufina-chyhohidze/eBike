package be.kdg.integration4.service.implementations;

import be.kdg.integration4.config.security.SecurityUtil;
import be.kdg.integration4.domain.enums.TestType;
import be.kdg.integration4.domain.report.BikeReport;
import be.kdg.integration4.domain.report.TestLine;
import be.kdg.integration4.repository.*;
import be.kdg.integration4.service.interfaces.BikeReportService;
import be.kdg.integration4.service.interfaces.BikeService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.ToDoubleFunction;
import java.util.stream.Collectors;

@Service
@Slf4j
public class BikeReportServiceImpl implements BikeReportService {

    private final BikeReportRepository bikeReportRepository;
    private final TestBenchRepository testBenchRepository;
    private final CustomerRepository customerRepository;
    private final BikeRepository bikeRepository;
    private final TestLineRepository testLineRepository;
    private final TechnicianRepository technicianRepository;

    public BikeReportServiceImpl(BikeReportRepository bikeReportRepository, TestBenchRepository testBenchRepository, CustomerRepository customerRepository, BikeService bikeService, BikeRepository bikeRepository, TestLineRepository testLineRepository, TechnicianRepository technicianRepository) {
        this.bikeReportRepository = bikeReportRepository;
        this.testBenchRepository = testBenchRepository;
        this.customerRepository = customerRepository;
        this.bikeRepository = bikeRepository;
        this.testLineRepository = testLineRepository;
        this.technicianRepository = technicianRepository;

        METRICS = new HashMap<>();
        METRICS.put("Battery Voltage (V)", TestLine::getBatteryVoltage);
        METRICS.put("Battery Current (A)", TestLine::getBatteryCurrent);
        METRICS.put("Battery Temperature (°C)", TestLine::getBatteryTemperature);
        METRICS.put("Engine Power (W)", TestLine::getEnginePower);
        METRICS.put("Wheel Power (W)", TestLine::getWheelPower);
        METRICS.put("Torque Crank (Nm)", TestLine::getTorqueCrank);
        METRICS.put("Cadence (RPM)", TestLine::getCadence);
        METRICS.put("Load Power (W)", TestLine::getLoadPower);
        METRICS.put("Bike Wheel Speed (km/h)", TestLine::getBikeWheelSpeed);
        METRICS.put("Engine RPM", TestLine::getEnginePower);  // Corrected to the appropriate method
        METRICS.put("Roll Torque (Nm)", TestLine::getRolTroque);  // Corrected method name
        METRICS.put("Loadcell (N)", TestLine::getLoadCell);
        METRICS.put("Roll Hz", TestLine::getRol);  // Corrected method name
        METRICS.put("Horizontal Inclination (°)", TestLine::getHorizontalInclinationSensor);
        METRICS.put("Vertical Inclination (°)", TestLine::getVerticalInclinationSensor);
        METRICS.put("Charge Status", TestLine::getChargeStatus);
        METRICS.put("Assistance Level", TestLine::getAssistanceLevel);
        METRICS.put("Status Plug", line -> line.getStatusPlug() ? 1.0 : 0.0);
    }

    private final Map<String, ToDoubleFunction<TestLine>> METRICS;





    @Override
    public BikeReport getById(Long id) {
        return bikeReportRepository.findById(id).orElse(null);
    }

    @Override
    public BikeReport getByIdWithTestlines(Long id) {
        return bikeReportRepository.findByIdWithTestLines(id);
    }

    @Override
    public List<BikeReport> getAll() {
        return bikeReportRepository.findAll();
    }

    @Transactional(readOnly = true)
    public List<BikeReport> getAllWithDetails() {
        return bikeReportRepository.findAllWithDetails();
    }


    @Override
    public BikeReport save(Long id, String bike, String reportDate, Integer score, String technician, String customer) {
        BikeReport bikeReport = new BikeReport(id, bike, reportDate, score, technician, customer);
        return bikeReportRepository.save(bikeReport);
    }

    @Override
    public void delete(Long id) {
        bikeReportRepository.delete(getById(id));
    }


    @Override
    public BikeReport save(Long testbenchNumber, TestType testType, String emailBikeOwner, String chassisNumber) {
        return bikeReportRepository.save(new BikeReport(bikeRepository.findBikeByFrameNumber(chassisNumber).orElse(null), LocalDate.now(), technicianRepository.findByEmail(SecurityUtil.getLoggedInUsername()), customerRepository.findByEmail(emailBikeOwner), testBenchRepository.getReferenceById(testbenchNumber)));
    }

    @Override
    public BikeReport update(Long id, String chassisNumber, LocalDate reportDate, Integer score, String technician, String customer, List<TestLine> testLines, Long benchId) {
        BikeReport bikeReport = bikeReportRepository.findById(id).orElseThrow();
        bikeReport.setBike(bikeRepository.findBikeByFrameNumber(chassisNumber).orElseThrow());
        bikeReport.setReportDate(reportDate);
        bikeReport.setScore(score);
        bikeReport.setTechnician(technicianRepository.findByEmail(technician));
        bikeReport.setCustomer(customerRepository.findByEmail(customer));
        testLineRepository.saveAll(testLines);
        bikeReport.setTestLines(testLines);
        bikeReport.setTestBench(testBenchRepository.findById(benchId).orElseThrow());
        return bikeReportRepository.save(bikeReport);
    }

    @Override
    //TODO: Do it with sql query instead
    public Map<String, Double> calculateAverages(List<TestLine> testLines) {
        return METRICS.entrySet().stream()
                .collect(Collectors.toMap(
                        Map.Entry::getKey,
                        entry -> testLines.stream().collect(Collectors.averagingDouble(entry.getValue()))
                ));
    }

    @Override
    public List<String> getFrameNumbersByCustomerId(Long customerId) {
        return this.getByCustomerId(customerId).stream().map(
                bikeReport -> bikeReport.getBike().getFrameNumber()
        ).collect(Collectors.toList());
    }

    @Override
    public List<BikeReport> getByCustomerId(Long customerId) {
        return this.bikeReportRepository.getBikeReportByCustomerId(customerId);
    }
}