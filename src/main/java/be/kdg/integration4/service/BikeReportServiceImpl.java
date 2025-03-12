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
    private final TechnicianRepository technicianRepository;

    public BikeReportServiceImpl(BikeReportRepository bikeReportRepository, TestBenchRepository testBenchRepository, CustomerRepository customerRepository, BikeService bikeService, BikeRepository bikeRepository, TechnicianRepository technicianRepository) {
        this.bikeReportRepository = bikeReportRepository;
        this.testBenchRepository = testBenchRepository;
        this.customerRepository = customerRepository;
        this.bikeService = bikeService;
        this.bikeRepository = bikeRepository;
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
    public void save(Long id, String bike, String reportDate, Integer score, String technician, String customer) {
        BikeReport bikeReport = new BikeReport(id, bike, reportDate, score, technician, customer);
        bikeReportRepository.save(bikeReport);
    }

    @Override
    public void delete(Long id) {
        bikeReportRepository.delete(findById(id));
    }


    @Override
    public void save(Long testbenchNumber, String testType, String emailBikeOwner, String chassisNumber) {
        bikeReportRepository.save(new BikeReport(bikeRepository.findBikeByFrameNumber(chassisNumber).orElse(null), LocalDate.now(), technicianRepository.findByEmail(SecurityUtil.getLoggedInUsername()), customerRepository.findByEmail(emailBikeOwner), testBenchRepository.getReferenceById(testbenchNumber)));
        log.debug("Bike: {}, Date: {}, Technician: {}, Customer: {}",
                bikeRepository.findBikeByFrameNumber(chassisNumber).orElse(null),
                LocalDate.now(),
                technicianRepository.findByEmail(SecurityUtil.getLoggedInUsername()),
                customerRepository.findByEmail(emailBikeOwner)
        );
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


}