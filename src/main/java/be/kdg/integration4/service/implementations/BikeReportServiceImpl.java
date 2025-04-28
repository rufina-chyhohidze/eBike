package be.kdg.integration4.service.implementations;

import be.kdg.integration4.config.security.SecurityUtil;
import be.kdg.integration4.domain.enums.TestType;
import be.kdg.integration4.domain.report.Bike;
import be.kdg.integration4.domain.report.BikeReport;
import be.kdg.integration4.domain.report.TestLine;
import be.kdg.integration4.repository.*;
import be.kdg.integration4.service.dtos.*;
import be.kdg.integration4.service.interfaces.BikeReportService;
import be.kdg.integration4.service.interfaces.BikeService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.crossstore.ChangeSetPersister;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.ToDoubleFunction;
import java.util.stream.Collectors;
import java.time.Duration;
import java.util.List;


@Transactional
@Service
@Slf4j
public class BikeReportServiceImpl implements BikeReportService {

    private final BikeReportRepository bikeReportRepository;
    private final TestBenchRepository testBenchRepository;
    private final CustomerRepository customerRepository;
    private final BikeService bikeService;
    private final BikeRepository bikeRepository;
    private final TestLineRepository testLineRepository;
    private final TechnicianRepository technicianRepository;

    public BikeReportServiceImpl(BikeReportRepository bikeReportRepository, TestBenchRepository testBenchRepository, CustomerRepository customerRepository, BikeService bikeService, BikeRepository bikeRepository, TestLineRepository testLineRepository, TechnicianRepository technicianRepository) {
        this.bikeReportRepository = bikeReportRepository;
        this.testBenchRepository = testBenchRepository;
        this.customerRepository = customerRepository;
        this.bikeService = bikeService;
        this.bikeRepository = bikeRepository;
        this.testLineRepository = testLineRepository;
        this.technicianRepository = technicianRepository;

    }






    @Override
    public BikeReport findById(Long id) {
        return bikeReportRepository.findById(id).orElse(null);
    }

    @Override
    public BikeReport findByIdWithTestlinesAndBike(Long id) {
        return bikeReportRepository.findByIdWithTestLinesAndBike(id);
    }

    @Override
    public List<BikeReport> findAll() {
        return bikeReportRepository.findAll();
    }

    @Transactional(readOnly = true)
    public List<BikeReport> getAllReportsWithDetails() {
        return bikeReportRepository.findAllWithDetails();
    }

    @Override
    public OverviewTestDTO calculateOverviewTest(Long reportId) {
        BikeReport report = bikeReportRepository.findById(reportId).orElse(null);

        Bike bike = report.getBike();

        List<TestLine> testLines = testLineRepository.findByBikeReportId(reportId);

        double measuredMaxEnginePower = testLines.stream().mapToDouble(TestLine::getEnginePower).max().orElse(0);
        double measuredMaxTorque = testLines.stream().mapToDouble(TestLine::getRolTorque).max().orElse(0);
        double measuredMaxWheelPower = testLines.stream().mapToDouble(TestLine::getWheelPower).max().orElse(0);

        double calculatedMaxSupport = (bike.getMaxSupport() * bike.getEnginePowerMax()) / measuredMaxEnginePower;

        double deviationEnginePower = ((measuredMaxEnginePower - bike.getEnginePowerMax()) / bike.getEnginePowerMax()) * 100;
        double deviationTorque = ((measuredMaxTorque - bike.getEngineTorque()) / bike.getEngineTorque()) * 100;
        double deviationMaxSupport = ((calculatedMaxSupport - bike.getMaxSupport()) / bike.getMaxSupport()) * 100;
        double deviationWheelPower = ((measuredMaxWheelPower - bike.getEnginePowerMax()) / bike.getEnginePowerMax()) * 100;

        double averageDeviation = (deviationEnginePower + deviationTorque + deviationMaxSupport + deviationWheelPower) / 4.0;
        double overviewScore = 100 - Math.abs(averageDeviation);

        return new OverviewTestDTO(
                measuredMaxEnginePower,
                deviationEnginePower,
                measuredMaxTorque,
                deviationTorque,
                calculatedMaxSupport,
                deviationMaxSupport,
                measuredMaxWheelPower,
                deviationWheelPower,
                overviewScore
        );
    }

    @Override
    public NominalLoadTestDTO calculateNominalLoadTest(Long reportId) {
        BikeReport report = bikeReportRepository.findById(reportId).orElse(null);
        if (report == null) {
            log.error("Bike report not found for reportId: {}", reportId);
            return null;
        }

        Bike bike = report.getBike();

        List<TestLine> testLines = testLineRepository.findByBikeReportId(reportId);

        double continuousLoad = testLines.stream().mapToDouble(TestLine::getEnginePower).average().orElse(0.0);

        double temperatureIncrease = testLines.stream()
                .mapToDouble(TestLine::getBatteryTemperature)
                .reduce((maxTemp, minTemp) -> Math.max(maxTemp, minTemp) - Math.min(maxTemp, minTemp))
                .orElse(0.0);

        double perfectTemperatureIncrease = 50.0;
        double score = 100.0 - (temperatureIncrease / perfectTemperatureIncrease) * 100.0;

        return new NominalLoadTestDTO(continuousLoad, temperatureIncrease, score);
    }



    public BatteryTestDTO calculateBatteryTest(Long reportId) {
        BikeReport report = bikeReportRepository.findById(reportId).orElseThrow();

        List<TestLine> testLines = testLineRepository.findByBikeReportId(reportId);

        boolean isFullyCharged = isFullyChargedAtStart(testLines);
        boolean isFullyDischarged = isFullyDischargedAtEnd(testLines);

        if (!isFullyCharged || !isFullyDischarged) {
            log.error("Battery was not fully charged at the start or not fully discharged at the end.");
            return null;
        }

        double availableCapacity = testLines.stream()
                .mapToDouble(r -> r.getBatteryVoltage() * r.getBatteryCurrent() / 3600)
                .sum();

        Double bikeBatteryCapacity = (double) report.getBike().getAccCapacity();

        double batteryHealth = availableCapacity / (bikeBatteryCapacity > 0 ? bikeBatteryCapacity : 1) * 100;

        Duration testDuration = calculateTestDuration(testLines);
        long testDurationInMinutes = testDuration.toMinutes();

        return new BatteryTestDTO(
                availableCapacity,
                batteryHealth,
                (int) Math.round(batteryHealth) // Rounded battery health as the score
        );
    }

    private boolean isFullyChargedAtStart(List<TestLine> testLines) {
        return true;
    }

    private boolean isFullyDischargedAtEnd(List<TestLine> testLines) {
        return true;
    }

    private Duration calculateTestDuration(List<TestLine> testLines) {
        if (testLines == null || testLines.isEmpty()) {
            return Duration.ZERO;
        }

        LocalDateTime startTime = testLines.stream()
                .map(TestLine::getDateTime)
                .min(LocalDateTime::compareTo)
                .orElse(LocalDateTime.now());  // Fallback to current time if no date_time is found

        LocalDateTime endTime = testLines.stream()
                .map(TestLine::getDateTime)
                .max(LocalDateTime::compareTo)
                .orElse(LocalDateTime.now());  // Fallback to current time if no date_time is found

        return Duration.between(startTime, endTime);
    }


    public BearingHealthDTO calculateBearingHealth(Long reportId) {
        BikeReport report = bikeReportRepository.findById(reportId).orElseThrow();

        List<TestLine> testLines = testLineRepository.findByBikeReportId(reportId);

        double goodVibrationThresholdHorizontal = 1.0;
        double goodVibrationThresholdVertical = 1.2;

        double maxHorizontalVibration = testLines.stream()
                .mapToDouble(TestLine::getHorizontalInclinationSensor)
                .max()
                .orElse(0);

        double maxVerticalVibration = testLines.stream()
                .mapToDouble(TestLine::getVerticalInclinationSensor)
                .max()
                .orElse(0);

        boolean bearingGood = isGoodBearing(
                maxHorizontalVibration,
                maxVerticalVibration,
                goodVibrationThresholdHorizontal,
                goodVibrationThresholdVertical
        );

        return new BearingHealthDTO(bearingGood);
    }

    private boolean isGoodBearing(double horizontalVibration, double verticalVibration, double goodHorizontalThreshold, double goodVerticalThreshold) {
        return horizontalVibration <= goodHorizontalThreshold && verticalVibration <= goodVerticalThreshold;
    }


    @Override
    public FullTestReportDTO getFullTestReport(Long reportId) {
        return null;
    }


    @Override
    public BikeReport save(Long id, String bike, String reportDate, Integer score, String technician, String customer) {
        BikeReport bikeReport = new BikeReport(id, bike, reportDate, score, technician, customer);
        return bikeReportRepository.save(bikeReport);
    }

    @Override
    public void delete(Long id) {
        bikeReportRepository.delete(findById(id));
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
    public List<String> getFrameNumbersByCustomerId(Long customerId) {
        return this.getBikeReportByCustomerId(customerId).stream().map(
                bikeReport -> bikeReport.getBike().getFrameNumber()
        ).collect(Collectors.toList());
    }

    @Override
    public List<BikeReport> getBikeReportByCustomerId(Long customerId) {
        return this.bikeReportRepository.getBikeReportByCustomerId(customerId);
    }
}