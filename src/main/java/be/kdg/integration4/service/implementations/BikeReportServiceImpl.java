package be.kdg.integration4.service.implementations;

import be.kdg.integration4.config.security.SecurityUtil;
import be.kdg.integration4.domain.enums.FunctionalTestComponents;
import be.kdg.integration4.domain.enums.InspectionCondition;
import be.kdg.integration4.domain.enums.TestType;
import be.kdg.integration4.domain.enums.VisualInspectionComponents;
import be.kdg.integration4.domain.profile.Technician;
import be.kdg.integration4.domain.report.Bike;
import be.kdg.integration4.domain.report.BikeReport;
import be.kdg.integration4.domain.report.TestLine;
import be.kdg.integration4.repository.*;
import be.kdg.integration4.service.dtos.*;
import be.kdg.integration4.service.interfaces.BikeReportService;
import be.kdg.integration4.service.interfaces.BikeService;
import be.kdg.integration4.service.interfaces.ReportSettingService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


@Transactional
@Service
@Slf4j
public class BikeReportServiceImpl implements BikeReportService {

    private final BikeReportRepository bikeReportRepository;
    private final TestBenchRepository testBenchRepository;
    private final CustomerRepository customerRepository;
    private final BikeRepository bikeRepository;
    private final TestLineRepository testLineRepository;
    private final TechnicianRepository technicianRepository;
    private final ReportSettingService reportSettingService;

    public BikeReportServiceImpl(BikeReportRepository bikeReportRepository, TestBenchRepository testBenchRepository, CustomerRepository customerRepository, BikeService bikeService, BikeRepository bikeRepository, TestLineRepository testLineRepository, TechnicianRepository technicianRepository, ReportSettingService reportSettingService) {
        this.bikeReportRepository = bikeReportRepository;
        this.testBenchRepository = testBenchRepository;
        this.customerRepository = customerRepository;
        this.bikeRepository = bikeRepository;
        this.testLineRepository = testLineRepository;
        this.technicianRepository = technicianRepository;
        this.reportSettingService = reportSettingService;
    }






    @Override
    public BikeReport getById(Long id) {
        return bikeReportRepository.findById(id).orElse(null);
    }

    @Override
    public BikeReport findByIdWithTestlinesAndBike(Long id) {
        return bikeReportRepository.findByIdWithTestLinesAndBikeAndVisualInspectionAndFunctionalTest(id);
    }

    @Override
    public List<BikeReport> getAll() {
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

        double calculatedMaxSupport = (bike.getBikeModel().getMaxSupport() * bike.getBikeModel().getEnginePowerMax()) / measuredMaxEnginePower;

        double deviationEnginePower = ((measuredMaxEnginePower - bike.getBikeModel().getEnginePowerMax()) / bike.getBikeModel().getEnginePowerMax()) * 100;
        double deviationTorque = ((measuredMaxTorque - bike.getBikeModel().getEngineTorque()) / bike.getBikeModel().getEngineTorque()) * 100;
        double deviationMaxSupport = ((calculatedMaxSupport - bike.getBikeModel().getMaxSupport()) / bike.getBikeModel().getMaxSupport()) * 100;
        double deviationWheelPower = ((measuredMaxWheelPower - bike.getBikeModel().getEnginePowerMax()) / bike.getBikeModel().getEnginePowerMax()) * 100;

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



    @Override
    public BatteryTestDTO calculateBatteryTest(Long reportId) {
        BikeReport report = bikeReportRepository.findById(reportId).orElseThrow();

        List<TestLine> testLines = testLineRepository.findByBikeReportId(reportId);

        boolean isFullyCharged = isFullyChargedAtStart(testLines);
        boolean isFullyDischarged = isFullyDischargedAtEnd(testLines);

        if (!isFullyCharged || !isFullyDischarged) {
            log.error("Battery was not fully charged at the start or not fully discharged at the end.");
            return null;
        }

        double availableCapacity = 0.0;
        for (int i = 1; i < testLines.size(); i++) {
            var prev = testLines.get(i - 1);
            var curr = testLines.get(i);

            double avgPower = (prev.getBatteryVoltage() * prev.getBatteryCurrent() +
                    curr.getBatteryVoltage() * curr.getBatteryCurrent()) / 2.0;

            Duration duration = Duration.between(prev.getDateTime(), curr.getDateTime());
            double durationHours = duration.toMillis() / 3600000.0;
            availableCapacity += avgPower * durationHours;
        }

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

    @Override
    public BearingHealthDTO calculateBearingHealth(Long reportId, Technician user) {
        BikeReport report = bikeReportRepository.findById(reportId).orElseThrow();

        List<TestLine> testLines = testLineRepository.findByBikeReportId(reportId);

        double goodVibrationThresholdHorizontal = reportSettingService.getReportSettingsForTechnician(user).getHorizontalVibration();
        double goodVibrationThresholdVertical = reportSettingService.getReportSettingsForTechnician(user).getVerticalVibration();

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
        bikeReportRepository.delete(getById(id));
    }

    @Override
    public BikeReport save(Long testbenchNumber, TestType testType, String emailBikeOwner, String chassisNumber) {
        return null;
    }

    @Override
    public BikeReport save(Long testbenchNumber, TestType testType, String emailBikeOwner, String chassisNumber, Map<String, InspectionCondition> inspection) {
        return null;
    }


    @Override
    public BikeReport save(Long testbenchNumber, TestType testType, String emailBikeOwner, String chassisNumber, Map<VisualInspectionComponents, InspectionCondition> inspection, Map<FunctionalTestComponents, InspectionCondition> functionalTest) {
        return bikeReportRepository.save(new BikeReport(bikeRepository.findBikeByFrameNumber(chassisNumber).orElse(null), LocalDate.now(), technicianRepository.findByEmail(SecurityUtil.getLoggedInUsername()), customerRepository.findByEmail(emailBikeOwner), testBenchRepository.getReferenceById(testbenchNumber), inspection, functionalTest));
    }

    @Override
    public BikeReport update(Long id, String chassisNumber, LocalDate reportDate, Integer score, String technician, String customer, List<TestLine> testLines, Long benchId) {
        BikeReport bikeReport = bikeReportRepository.findById(id).orElseThrow();
        bikeReport.setBike(bikeRepository.findBikeByFrameNumberWithBikeModel(chassisNumber).orElseThrow());
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
        return this.getByCustomerId(customerId).stream().map(
                bikeReport -> bikeReport.getBike().getFrameNumber()
        ).collect(Collectors.toList());
    }

    @Override
    public List<BikeReport> getByCustomerId(Long customerId) {
        return this.bikeReportRepository.getBikeReportByCustomerId(customerId);
    }
}