package be.kdg.integration4.controller.api;

import be.kdg.integration4.controller.api.dtos.TestLineDto;
import be.kdg.integration4.controller.api.dtos.TestLineMapper;
import be.kdg.integration4.domain.BikeReport;
import be.kdg.integration4.service.interfaces.BikeReportService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/reports")
public class TestReportsController {

    private final BikeReportService bikeReportService;
    private final TestLineMapper testLineMapper;

    public TestReportsController(BikeReportService bikeReportService, TestLineMapper testLineMapper) {
        this.bikeReportService = bikeReportService;
        this.testLineMapper = testLineMapper;
    }

    @GetMapping("/{id}")
    public ResponseEntity<List<TestLineDto>> testLines(@PathVariable("id") String id) {
            BikeReport bikeReport = bikeReportService.findByIdWithTestlines(Long.valueOf(id));
            List<TestLineDto> testLines = testLineMapper.toTestLineDtoList(bikeReport.getTestLines());
            return ResponseEntity.ok(testLines);
    }
}
