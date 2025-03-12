package be.kdg.integration4.service;

import be.kdg.integration4.config.SecurityUtil;
import be.kdg.integration4.domain.Customer;
import be.kdg.integration4.domain.Technician;
import be.kdg.integration4.domain.TestType;
import be.kdg.integration4.domain.BikeReport;
import be.kdg.integration4.repository.*;
import lombok.extern.slf4j.Slf4j;
import be.kdg.integration4.domain.*;
import be.kdg.integration4.exceptions.CSVException;
import be.kdg.integration4.repository.BikeReportRepository;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.util.List;
import java.util.*;
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
    public List<TestLine> csvConverter(MultipartFile file, BikeReport bikeReport) {
        if (file.isEmpty()) {
            throw new CSVException("CSV file is empty");
        }

        try (BufferedReader reader = new BufferedReader(new InputStreamReader(file.getInputStream(), StandardCharsets.UTF_8))) {
            // Read through the file to find the header row that starts with "Date / Time"
            String line;
            String headerLine = null;
            while ((line = reader.readLine()) != null) {
                // Check for the header row
                if (line.startsWith("Date / Time")) {
                    headerLine = line;
                    break;
                }
            }

            // If no header row is found, return an error
            if (headerLine == null) {
                throw new CSVException("CSV file is empty");
            }

            // Now, prepare to parse the CSV with the found header
            CSVParser csvParser = new CSVParser(reader, CSVFormat.DEFAULT.withHeader(headerLine.split(",")));

            List<TestLine> testLines = new ArrayList<>();

            for (CSVRecord record : csvParser) {
                TestLine testLine = new TestLine(
                        TestLine.parseDateTime(record.get("Date / Time")),
                        Float.parseFloat(record.get("Battery Voltage (V)")),
                        Float.parseFloat(record.get("Battery Current (A)")),
                        Float.parseFloat(record.get("Battery Capacity (%)")),
                        Float.parseFloat(record.get("Battery Temperature (°C)")),
                        Integer.parseInt(record.get("Charge Status")),
                        Integer.parseInt(record.get("Assistance level")),
                        Float.parseFloat(record.get("Torque Crank (Nm)")),
                        Float.parseFloat(record.get("Bike wheel speed (km/h)")),
                        Integer.parseInt(record.get("Cadance (rpm)")),
                        Integer.parseInt(record.get("Engine (rpm)")),
                        Float.parseFloat(record.get("Engine Power (W)")),
                        Float.parseFloat(record.get("Wheel Power (W)")),
                        Float.parseFloat(record.get("Rol Troque (Nm)")),
                        Float.parseFloat(record.get("Loadcell (N)")),
                        Float.parseFloat(record.get("Rol (Hz)")),
                        Float.parseFloat(record.get("Hozizontal inclination sensor")),
                        Float.parseFloat(record.get("Vertical inclination sensor")),
                        Integer.parseInt(record.get("Load Power (%)")),
                        TestLine.parseStatusPlug(record.get("Status Plug"))
                );
                testLines.add(testLine);
            }

            testLines.forEach(System.err::println);

            bikeReport.setTestLines(testLines);
            bikeReportRepository.save(bikeReport);
            return bikeReport.getTestLines();

        } catch (Exception e) {
            throw new CSVException("CSV processing failed");
        }

    }

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
