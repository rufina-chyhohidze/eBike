package be.kdg.integration4.CSVProcessor;

import be.kdg.integration4.domain.TestLine;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/csv")
public class CSVsReceiverController {

    @PostMapping("/receive")
    public ResponseEntity<String> uploadCSV(@RequestParam("file") MultipartFile file) {
        if (file.isEmpty()) {
            return ResponseEntity.badRequest().body("Please upload a valid CSV file");
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
                return ResponseEntity.badRequest().body("Header row 'Date / Time' not found.");
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

            return ResponseEntity.ok("CSV Processed Successfully: " + testLines);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error processing CSV: " + e.getMessage());
        }
    }
}


//working:
//package be.kdg.integration4.CSVProcessor;
//
//import org.apache.commons.csv.CSVFormat;
//import org.apache.commons.csv.CSVParser;
//import org.apache.commons.csv.CSVRecord;
//import org.springframework.http.ResponseEntity;
//import org.springframework.web.bind.annotation.PostMapping;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.RequestParam;
//import org.springframework.web.bind.annotation.RestController;
//import org.springframework.web.multipart.MultipartFile;
//
//import java.io.BufferedReader;
//import java.io.InputStreamReader;
//import java.nio.charset.StandardCharsets;
//import java.util.ArrayList;
//import java.util.List;
//
//@RestController
//@RequestMapping("/api/csv")
//public class CSVsReceiverController {
//
//    @PostMapping("/receive")
//    public ResponseEntity<String> uploadCSV(@RequestParam("file") MultipartFile file) {
//        if (file.isEmpty()) {
//            return ResponseEntity.badRequest().body("Please upload a valid CSV file");
//        }
//
//        try (BufferedReader reader = new BufferedReader(new InputStreamReader(file.getInputStream(), StandardCharsets.UTF_8))) {
//            // Read through the file to find the header row that starts with "Date / Time"
//            String line;
//            String headerLine = null;
//            while ((line = reader.readLine()) != null) {
//                // Check for the header row
//                if (line.startsWith("Date / Time")) {
//                    headerLine = line;
//                    break;
//                }
//            }
//
//            // If no header row is found, return an error
//            if (headerLine == null) {
//                return ResponseEntity.badRequest().body("Header row 'Date / Time' not found.");
//            }
//
//            // Now, prepare to parse the CSV with the found header
//            CSVParser csvParser = new CSVParser(reader, CSVFormat.DEFAULT.withHeader(headerLine.split(",")));
//
//            List<String> records = new ArrayList<>();
//
//            for (CSVRecord record : csvParser) {
//                StringBuilder row = new StringBuilder();
//
//                // Loop through all column headers
//                for (String header : record.toMap().keySet()) {
//                    row.append(header).append(": ").append(record.get(header)).append(", ");
//                }
//
//                records.add(row.toString());
//            }
//
//            return ResponseEntity.ok("CSV Processed Successfully: " + records);
//        } catch (Exception e) {
//            return ResponseEntity.badRequest().body("Error processing CSV: " + e.getMessage());
//        }
//    }
//}