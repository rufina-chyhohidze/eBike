package be.kdg.integration4.controller.api;

import be.kdg.integration4.config.security.annotations.StaffOnly;
import be.kdg.integration4.config.security.annotations.TechnicianOnly;
import be.kdg.integration4.controller.api.dtos.ShortBikeReportDto;
import be.kdg.integration4.controller.api.dtos.TestDto;
import be.kdg.integration4.controller.api.dtos.TestIdDto;
import be.kdg.integration4.controller.api.dtos.TestLineDto;
import be.kdg.integration4.domain.enums.UserRole;
import be.kdg.integration4.domain.profile.Customer;
import be.kdg.integration4.domain.profile.Technician;
import be.kdg.integration4.domain.profile.User;
import be.kdg.integration4.domain.profile.UserDetailsImpl;
import be.kdg.integration4.domain.report.Bike;
import be.kdg.integration4.domain.report.BikeReport;
import be.kdg.integration4.service.email.EmailService;
import be.kdg.integration4.service.implementations.BikeReportServiceImpl;
import be.kdg.integration4.service.interfaces.*;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@RestController
@RequestMapping("/api/reports")
public class ReportsController {

    private final BikeReportService bikeReportService;
    private final BikeService bikeService;
    private final TestbenchApiService testbenchApiService;
    private final EmailService emailService;

    public ReportsController(
            BikeReportService bikeReportService,
            BikeService bikeService,
            TestbenchApiService testbenchApiService,
            EmailService emailService) {
        this.bikeReportService = bikeReportService;
        this.bikeService = bikeService;
        this.testbenchApiService = testbenchApiService;
        this.emailService = emailService;
    }

    @GetMapping("/{id}")
    public ResponseEntity<List<TestLineDto>> testLines(@PathVariable("id") String id) {
        BikeReport bikeReport = bikeReportService.findByIdWithTestlinesAndBike(Long.valueOf(id));
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
                        t.getRolTorque(),
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
                test.getFrameNumber(),
                test.getVisualInspection(),
                test.getFunctionalTest());

        Bike bike = bikeService.getByFrameNumber(test.getFrameNumber()).orElseThrow();

        // Pass visual inspection data to the testbench API service (if required)
        String id = testbenchApiService.startTest(
                test.getTestType(),
                bike.getAccCapacity(),
                (int) Math.round(bike.getBikeModel().getMaxSupport()),
                bike.getBikeModel().getEnginePowerMax(),
                bike.getBikeModel().getEnginePowerNominal(),
                bike.getBikeModel().getEngineTorque()
        ).id();

        // Save the API request if necessary
        testbenchApiService.saveApiRequest(report, id);
        return ResponseEntity.ok(new TestIdDto(id));
    }

    // todo: handle codes in JS
    @PostMapping("{reportId}/customer")
    public ResponseEntity<Void> sendReportURLToCustomer(
            @PathVariable("reportId") Long reportId
    ) {
        try {
            if (reportId == null) return ResponseEntity.notFound().build();

            log.info("Sending report to customer email - reportId: {}", reportId);
            this.emailService.sendReportURLToCustomer(reportId);
        } catch (Exception e) {
            log.error("Error sending report url to customer: {}", e.getMessage());
            return ResponseEntity.badRequest().build();
        }

        return ResponseEntity.ok().build();
    }



    @GetMapping
    public ResponseEntity<List<ShortBikeReportDto>> filterReports(
            @RequestParam(required = false) String frameNumber,
            @RequestParam(required = false) String engineType,
            @AuthenticationPrincipal UserDetailsImpl principal
    ) {
        List<BikeReport> reports = bikeReportService.getReportsAccessibleByUserWithId(principal.getUserId());
        log.debug("Reports found: {}", reports.size());
        List<BikeReport> filteredReports = bikeReportService.filterReports(reports, frameNumber, engineType);
        log.debug("Filtered reports found: {}", filteredReports.size());
        return ResponseEntity.ok(filteredReports.stream().map(report -> new ShortBikeReportDto(
                report.getId(),
                report.getReportDate(),
                report.getCustomer().getId(),
                report.getBike().getFrameNumber(),
                report.getBike().getBikeModel().getEngineType(),
                report.getTestBench().getBenchId()
        )).toList());
    }


    @PostMapping("/{id}/email")
    public ResponseEntity<Void> sendReportToCustomer(@PathVariable("id") Long id) {
        emailService.sendReportURLToCustomer(id);
        return ResponseEntity.ok().build();
    }
}
