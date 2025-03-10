package be.kdg.integration4.service;

import be.kdg.integration4.domain.BikeReport;
import be.kdg.integration4.domain.TestLine;
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
import java.util.*;

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
    public void save(Long id, String bike, String reportDate, Integer score, String technician, String customer) {
        BikeReport bikeReport = new BikeReport(id, bike, reportDate, score, technician, customer);
        bikeReportRepository.save(bikeReport);
    }

    @Override
    public void delete(Long id) {
        bikeReportRepository.delete(findById(id));
    }

    @Override
    public void csvConverter(MultipartFile file) {
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

        } catch (Exception e) {
            throw new CSVException("CSV processing failed");
        }

        Map<String, String> extractedData = new LinkedHashMap<>();

        try (BufferedReader reader = new BufferedReader(new InputStreamReader(file.getInputStream(), StandardCharsets.UTF_8))) {
            List<String[]> rows = new ArrayList<>();
            String line;

            while ((line = reader.readLine()) != null) {
                rows.add(line.split(",", -1));
            }

            if (rows.size() < 4) {
                throw new CSVException("....");
            }

            System.out.println(Arrays.toString(rows.get(4)));

            extractedData.put("Workshop Name", rows.get(1)[2].trim());
            extractedData.put("Workshop Location", rows.get(2)[2].trim() + ", " + rows.get(2)[3].trim());
            extractedData.put("Testbench Number", rows.get(3)[2].trim());
            extractedData.put("Review Date", rows.get(4)[2].trim());

            extractedData.put("Mechanic Name", rows.get(1)[5].trim() + " " + rows.get(1)[6]);
            extractedData.put("Bike Owner", rows.get(2)[5].trim() + " " + rows.get(2)[6]);

            extractedData.put("Brand", rows.get(1)[9].trim());
            extractedData.put("Bike Type", rows.get(2)[9].trim());
            extractedData.put("Chassis Number", rows.get(3)[9].trim());
            extractedData.put("Production Date", rows.get(4)[9].trim());

            extractedData.put("Bike Size", rows.get(1)[11].trim());
            extractedData.put("Mileage (km)", rows.get(2)[11].trim());
            extractedData.put("Gear Type", rows.get(3)[11].trim());
            extractedData.put("Engine Type", rows.get(4)[11].trim());

            extractedData.put("Powertrain", rows.get(1)[13].trim());
            extractedData.put("Accu Capacity (Wh)", rows.get(2)[13].trim());
            extractedData.put("Max Support (%)", rows.get(3)[13].trim());


            extractedData.put("Engine Power - Max (W)", rows.get(1)[15].trim());
            extractedData.put("Engine Power - Nominal (W)", rows.get(2)[15].trim());
            extractedData.put("Engine Torque (Nm)", rows.get(3)[15].trim());

        } catch (Exception e) {
            throw new CSVException("CSV processing failed");
        }

        //save(1, extractedData.get("Chassis Number"))
    }
}
