package be.kdg.integration4.CSVProcessor;

import be.kdg.integration4.domain.TestLine;
import be.kdg.integration4.service.BikeReportServiceImpl;
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

    private final BikeReportServiceImpl bikeReportServiceImpl;

    public CSVsReceiverController(BikeReportServiceImpl bikeReportServiceImpl) {
        this.bikeReportServiceImpl = bikeReportServiceImpl;
    }

    @PostMapping("/receive")
    public ResponseEntity<String> uploadCSV(@RequestParam("file") MultipartFile file) {
        bikeReportServiceImpl.csvConverter(file);
        return ResponseEntity.ok().body("OK");
    }

    @PostMapping("/extract")
    public ResponseEntity<Map<String, String>> extractData(@RequestParam("file") MultipartFile file) {
        if (file.isEmpty()) {
            return ResponseEntity.badRequest().body(Collections.singletonMap("error", "Please upload a valid CSV file"));
        }


    }


}
