package be.kdg.integration4.service;

import be.kdg.integration4.domain.TestLine;

import java.util.List;

public interface CsvProcessingService {
    List<TestLine> processCsvString(String csvContent);
}
