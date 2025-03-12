package be.kdg.integration4.service;

import be.kdg.integration4.domain.TestLine;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@Service
public class CsvProcessingServiceImpl implements CsvProcessingService {
    public static List<TestLine> processCsvString(String csvContent) {
        List<TestLine> testLines = new ArrayList<>();
        String[] lines = csvContent.split("\\r?\\n");
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss.SSS");

        for (int i = 1; i < lines.length; i++) { // Skipping header
            String[] values = lines[i].split(",");
            if (values.length < 20) continue; // Skip invalid rows

            TestLine testLine = new TestLine(
                    LocalDateTime.parse(values[0], formatter),
                    parseFloat(values[1]),
                    parseFloat(values[2]),
                    parseFloat(values[3]),
                    parseFloat(values[4]),
                    parseInt(values[5]),
                    parseInt(values[6]),
                    parseFloat(values[7]),
                    parseFloat(values[8]),
                    parseInt(values[9]),
                    parseInt(values[10]),
                    parseFloat(values[11]),
                    parseFloat(values[12]),
                    parseFloat(values[13]),
                    parseFloat(values[14]),
                    parseFloat(values[15]),
                    parseFloat(values[16]),
                    parseFloat(values[17]),
                    parseInt(values[18]),
                    parseBoolean(values[19])
            );
            testLines.add(testLine);
        }
        return testLines;
    }

    private static Float parseFloat(String value) {
        return value.isEmpty() ? null : Float.parseFloat(value);
    }

    private static Integer parseInt(String value) {
        return value.isEmpty() ? null : Integer.parseInt(value);
    }

    private static Boolean parseBoolean(String value) {
        return "TRUE".equalsIgnoreCase(value);
    }
}
