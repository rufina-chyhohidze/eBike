package be.kdg.integration4.service;

import be.kdg.integration4.config.SecurityUtil;
import be.kdg.integration4.domain.*;
import be.kdg.integration4.repository.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

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
    public List<BikeReport> findAll() {
        return bikeReportRepository.findAll();
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
    public Map<String, Double> calculateAverages(List<TestLine> testLines) {
        return Map.of(
                "Battery Voltage (V)", testLines.stream().collect(Collectors.averagingDouble(TestLine::getBatteryVoltage)),
                "Battery Current (A)", testLines.stream().collect(Collectors.averagingDouble(TestLine::getBatteryCurrent)),
                "Battery Temperature (°C)", testLines.stream().collect(Collectors.averagingDouble(TestLine::getBatteryTemperature)),
                "Engine Power (W)", testLines.stream().collect(Collectors.averagingDouble(TestLine::getEnginePower)),
                "Wheel Power (W)", testLines.stream().collect(Collectors.averagingDouble(TestLine::getWheelPower))
        );
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