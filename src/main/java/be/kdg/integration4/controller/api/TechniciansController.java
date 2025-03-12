package be.kdg.integration4.controller.api;

import be.kdg.integration4.domain.BikeSize;
import be.kdg.integration4.controller.api.dtos.TestDto;
import be.kdg.integration4.service.BikeReportService;
import be.kdg.integration4.service.BikeService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;

@RestController
@RequestMapping("/api")
@Slf4j
public class TechniciansController {
    private final BikeReportService bikeReportService;
    private final BikeService bikeService;

    public TechniciansController(BikeReportService bikeReportService, BikeService bikeService) {
        this.bikeReportService = bikeReportService;
        this.bikeService = bikeService;
    }

    @PostMapping("/start-test")
    public ResponseEntity<String> startTest(@RequestBody TestDto test) {
        if (bikeService.findByFrameNumber(test.getChassisNumber()) == null) {
            bikeService.save(
                    test.getChassisNumber(),
                    test.getType(),
                    test.getBikeBrand(),
                    LocalDate.now(),
                    LocalDate.of(test.getProductionDate(), 1, 1),
                    BikeSize.valueOf(test.getBikeSize()),
                    test.getMileage(),
                    test.getGearType(),
                    test.getEngineType(),
                    test.getPowertrain(),
                    test.getAccuCapacity(),
                    test.getMaxSupport(),
                    test.getEnginePowerMax(),
                    test.getEnginePowerNominal(),
                    test.getEngineTorque()
            );
        }

        log.debug(String.valueOf((long) test.getTestbenchNumber()));



        bikeReportService.save(
                (long) test.getTestbenchNumber(),
                test.getTestType(),
                test.getEmailBikeOwner(),
                test.getChassisNumber()
        );

        return ResponseEntity.ok("Test started successfully!");
    }


}
