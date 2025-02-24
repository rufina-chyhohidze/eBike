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
import java.util.*;

@RestController
@RequestMapping("/api/csv")
public class CSVsReceiverController {

    @PostMapping("/receive")
    public ResponseEntity<String> uploadCSV(@RequestParam("file") MultipartFile file) {
        if (file.isEmpty()) {
            return ResponseEntity.badRequest().body("Please upload a valid CSV file");
        }

        try (BufferedReader reader = new BufferedReader(new InputStreamReader(file.getInputStream(), StandardCharsets.UTF_8))) {
            // Skip the initial rows before the header row
            String line;
            while ((line = reader.readLine()) != null) {
                // Check for the header row (you can adjust this based on the actual header content)
                if (line.startsWith("Date / Time")) {  // This is where your header starts
                    break;
                }
            }

            // Now create CSVParser with the remaining content starting from the header row
            CSVParser csvParser = new CSVParser(reader, CSVFormat.DEFAULT.withFirstRecordAsHeader());

            List<String> records = new ArrayList<>();

            for (CSVRecord record : csvParser) {
                StringBuilder row = new StringBuilder();

                for (String header : record.toMap().keySet()) { // Loop through all column headers
                    row.append(header).append(": ").append(record.get(header)).append(", ");
                }

                records.add(row.toString());
            }

            return ResponseEntity.ok("CSV Processed Successfully: " + records);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error processing CSV: " + e.getMessage());
        }
    }

    @PostMapping("/extract")
    public ResponseEntity<Map<String, String>> extractData(@RequestParam("file") MultipartFile file) {
        if (file.isEmpty()) {
            return ResponseEntity.badRequest().body(Collections.singletonMap("error", "Please upload a valid CSV file"));
        }

        Map<String, String> extractedData = new LinkedHashMap<>();

        try (BufferedReader reader = new BufferedReader(new InputStreamReader(file.getInputStream(), StandardCharsets.UTF_8))) {
            List<String[]> rows = new ArrayList<>();
            String line;

            while ((line = reader.readLine()) != null) {
                rows.add(line.split(",", -1));
            }

            if (rows.size() < 4) {
                return ResponseEntity.badRequest().body(Collections.singletonMap("error", "CSV does not have enough data"));
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

            return ResponseEntity.ok(extractedData);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Collections.singletonMap("error", "Error processing CSV: " + e.getMessage()));
        }
    }


}
