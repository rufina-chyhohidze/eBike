package be.kdg.integration4.service;

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
import java.util.*;
import java.util.stream.Collectors;

@Service
public class BikeReportServiceImpl implements BikeReportService {

    private final BikeReportRepository bikeReportRepository;

    public BikeReportServiceImpl(BikeReportRepository bikeReportRepository) {
        this.bikeReportRepository = bikeReportRepository;
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
    public void save(Bike bike, LocalDate reportDate, Integer score, Technician technician, Customer customer, List<TestLine> testLines) {
        BikeReport bikeReport = new BikeReport(bike, reportDate, score, technician, customer, testLines);
    }


    @Override
    public void delete(Long id) {
        bikeReportRepository.delete(findById(id));
    }

    @Override
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
