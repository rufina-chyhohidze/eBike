package be.kdg.integration4.controller.api;

import be.kdg.integration4.config.security.annotations.TechnicianOnly;
import be.kdg.integration4.controller.api.dtos.TestDto;
import be.kdg.integration4.controller.api.dtos.TestIdDto;
import be.kdg.integration4.controller.api.dtos.TestLineDto;
import be.kdg.integration4.domain.profile.Customer;
import be.kdg.integration4.domain.profile.Technician;
import be.kdg.integration4.domain.report.Bike;
import be.kdg.integration4.domain.report.BikeReport;
import be.kdg.integration4.service.email.EmailService;
import be.kdg.integration4.service.interfaces.BikeReportService;
import be.kdg.integration4.service.interfaces.BikeService;
import be.kdg.integration4.service.interfaces.TechnicianService;
import be.kdg.integration4.service.interfaces.TestbenchApiService;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
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
    private final TechnicianService technicianService;

    public ReportsController(
            BikeReportService bikeReportService,
            BikeService bikeService,
            TestbenchApiService testbenchApiService,
            EmailService emailService, TechnicianService technicianService
    ) {
        this.bikeReportService = bikeReportService;
        this.bikeService = bikeService;
        this.testbenchApiService = testbenchApiService;
        this.emailService = emailService;
        this.technicianService = technicianService;
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
                (int) Math.round(bike.getMaxSupport()),
                bike.getEnginePowerMax(),
                bike.getEnginePowerNominal(),
                bike.getEngineTorque()
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



//    @GetMapping()
//    @TechnicianOnly
//    public ResponseEntity<List<BikeReport>> filterReports(
//            @RequestParam(required = false) String frameNumber,
//            @RequestParam(required = false) String bikeModel,
//            @RequestParam(required = false) String brand) {
//
//        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
//        String loggedInEmail = authentication.getName();
//        Technician technician = technicianService.getByEmail(loggedInEmail);
//
//        List<BikeReport> bikeReports = bikeReportService.getAll()
//                .stream()
//                .filter(report -> report.getTechnician().getId().equals(technician.getId()))
//                .collect(Collectors.toList());
//
//        if (frameNumber != null && !frameNumber.isEmpty()) {
//            String frameNumberLower = frameNumber.toLowerCase();
//            bikeReports = bikeReports.stream()
//                    .filter(report -> report.getBike().getFrameNumber().toLowerCase().contains(frameNumberLower))
//                    .collect(Collectors.toList());
//        }
//
//        if (bikeModel != null && !bikeModel.isEmpty()) {
//            String bikeModelLower = bikeModel.toLowerCase();
//            bikeReports = bikeReports.stream()
//                    .filter(report -> report.getBike().getType().toLowerCase().contains(bikeModelLower))
//                    .collect(Collectors.toList());
//        }
//
//        if (brand != null && !brand.isEmpty()) {
//            String brandLower = brand.toLowerCase();
//            bikeReports = bikeReports.stream()
//                    .filter(report -> report.getBike().getBrand().toLowerCase().contains(brandLower))
//                    .collect(Collectors.toList());
//        }
//
//        return ResponseEntity.ok(bikeReports);
//    }

}
