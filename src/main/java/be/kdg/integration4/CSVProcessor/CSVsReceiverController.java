package be.kdg.integration4.CSVProcessor;

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

            List<String> records = new ArrayList<>();

            for (CSVRecord record : csvParser) {
                StringBuilder row = new StringBuilder();

                // Loop through all column headers
                for (String header : record.toMap().keySet()) {
                    row.append(header).append(": ").append(record.get(header)).append(", ");
                }

                records.add(row.toString());
            }

            return ResponseEntity.ok("CSV Processed Successfully: " + records);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error processing CSV: " + e.getMessage());
        }
    }
}


//
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
//            // Skip the initial rows before the header row
//            String line;
//            while ((line = reader.readLine()) != null) {
//                // Check for the header row (you can adjust this based on the actual header content)
//                if (line.startsWith("Date / Time")) {  // This is where your header starts
//                    break;
//                }
//            }
//
//            // Now create CSVParser with the remaining content starting from the header row
//            CSVParser csvParser = new CSVParser(reader, CSVFormat.DEFAULT.withFirstRecordAsHeader());
//
//            List<String> records = new ArrayList<>();
//
//            for (CSVRecord record : csvParser) {
//                StringBuilder row = new StringBuilder();
//
//                for (String header : record.toMap().keySet()) { // Loop through all column headers
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
//
//}
