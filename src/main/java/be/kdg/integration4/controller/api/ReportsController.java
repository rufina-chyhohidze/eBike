package be.kdg.integration4.controller.api;

import be.kdg.integration4.config.security.annotations.TechnicianOnly;
import be.kdg.integration4.controller.api.dtos.CustomMapper;
import be.kdg.integration4.controller.api.dtos.TestDto;
import be.kdg.integration4.controller.api.dtos.TestIdDto;
import be.kdg.integration4.controller.api.dtos.TestLineDto;
import be.kdg.integration4.domain.report.Bike;
import be.kdg.integration4.domain.report.BikeReport;
import be.kdg.integration4.service.interfaces.BikeReportService;
import be.kdg.integration4.service.interfaces.BikeService;
import be.kdg.integration4.service.interfaces.TestbenchApiService;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/api/reports")
public class ReportsController {

    private final BikeReportService bikeReportService;
    private final BikeService bikeService;
    private final TestbenchApiService testbenchApiService;

    public ReportsController(BikeReportService bikeReportService, BikeService bikeService, TestbenchApiService testbenchApiService, CustomMapper customMapper) {
        this.bikeReportService = bikeReportService;
        this.bikeService = bikeService;
        this.testbenchApiService = testbenchApiService;
    }

    @GetMapping("/{id}")
    public ResponseEntity<List<TestLineDto>> testLines(@PathVariable("id") String id) {
        BikeReport bikeReport = bikeReportService.findByIdWithTestlines(Long.valueOf(id));
        List<TestLineDto> testLines = bikeReport.getTestLines().stream()
                .map(t -> new TestLineDto(
                        t.getId(),
                        t.getDateTime(),
                        t.getBatteryVoltage(),
                        t.getBatteryCurrent(),
                        t.getBatteryCapacity(),
                        t.getBatteryTemperature(),
                        t.getChargeStatus(),
                        t.getAssistanceLevel(),
                        t.getTorqueCrank(),
                        t.getBikeWheelSpeed(),
                        t.getCadence(),
                        t.getEngineRPM(),
                        t.getEnginePower(),
                        t.getWheelPower(),
                        t.getRolTroque(),
                        t.getLoadCell(),
                        t.getRol(),
                        t.getHorizontalInclinationSensor(),
                        t.getVerticalInclinationSensor(),
                        t.getLoadPower(),
                        t.getStatusPlug()
                ))
                .toList();
        return ResponseEntity.ok(testLines);
    }

    @PostMapping
    @TechnicianOnly
    public ResponseEntity<TestIdDto> startTest(@RequestBody @Valid TestDto test) {
        BikeReport report = bikeReportService.save(
                (long) test.getTestBenchNumber(),
                test.getTestType(),
                test.getEmailBikeOwner(),
                test.getFrameNumber()
        );

        Bike bike = bikeService.findByFrameNumber(test.getFrameNumber()).orElseThrow();

        String id = testbenchApiService.startTest(test.getTestType(),
                bike.getAccCapacity(),
                (int) Math.round(bike.getMaxSupport()),
                bike.getEnginePowerMax(),
                bike.getEnginePowerNominal(),
                bike.getEngineTorque()).id();

        testbenchApiService.saveApiRequest(report, id);
        return ResponseEntity.ok(new TestIdDto(id));
    }
}
