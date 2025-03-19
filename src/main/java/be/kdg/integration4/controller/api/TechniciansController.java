package be.kdg.integration4.controller.api;

import be.kdg.integration4.controller.api.dtos.BikeDto;
import be.kdg.integration4.controller.api.dtos.CustomMapper;
import be.kdg.integration4.domain.Bike;
import be.kdg.integration4.domain.BikeReport;
import be.kdg.integration4.domain.BikeSize;
import be.kdg.integration4.controller.api.dtos.TestDto;
import be.kdg.integration4.service.BikeReportService;
import be.kdg.integration4.service.BikeService;
import be.kdg.integration4.service.TechnicianService;
import be.kdg.integration4.service.TestbenchApiService;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api")
@Slf4j
public class TechniciansController {
    private final BikeReportService bikeReportService;
    private final BikeService bikeService;
    private final TestbenchApiService testbenchApiService;
    private final CustomMapper customMapper;

    public TechniciansController(BikeReportService bikeReportService, BikeService bikeService, TestbenchApiService testbenchApiService, CustomMapper customMapper) {
        this.bikeReportService = bikeReportService;
        this.bikeService = bikeService;
        this.testbenchApiService = testbenchApiService;
        this.customMapper = customMapper;
    }

    @PostMapping("/save/bike")
    public ResponseEntity<Void> createNewBike(
            @RequestBody @Valid BikeDto bikeDto,
            BindingResult bindingResult
    ) {
        if (bindingResult.hasErrors()) {
            log.error("Validation errors: {}", bindingResult.getAllErrors());
            return ResponseEntity.badRequest().build();
        }
        log.info("No errors: saving bike {}", bikeDto);
        this.bikeService.save(
                bikeDto.frameNumber(),
                bikeDto.bikeOwnerId(),
                bikeDto.type(),
                bikeDto.brand(),
                LocalDateTime.now(),
                LocalDate.of(bikeDto.productionDate().getYear(), 1, 1),
                bikeDto.bikeSize(),
                bikeDto.milleage(),
                bikeDto.gearType(),
                bikeDto.engineType(),
                bikeDto.powertrain(),
                bikeDto.accCapacity(),
                bikeDto.maxSupport(),
                bikeDto.enginePowerMax(),
                bikeDto.enginePowerNominal(),
                bikeDto.engineTorque()
        );

        if (bikeService.findByFrameNumber(bikeDto.frameNumber()).isEmpty()) return ResponseEntity.notFound().build();
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/start-test")
    //TODO: Add dto validation
    public ResponseEntity<Map<String, String>> startTest(@RequestBody TestDto test) {
        BikeReport report = bikeReportService.save(
                (long) test.getTestBenchNumber(),
                test.getTestType(),
                test.getEmailBikeOwner(),
                test.getFrameNumber()
        );

        Bike bike = bikeService.findByFrameNumber(test.getFrameNumber()).orElseThrow();

        String id = testbenchApiService.sendStartRequest(test.getTestType(),
                bike.getAccCapacity(),
                (int) Math.round(bike.getMaxSupport()),
                bike.getEnginePowerMax(),
                bike.getEnginePowerNominal(),
                bike.getEngineTorque()).id();

        testbenchApiService.saveApiRequest(report,id);
        Map<String,String> response = new HashMap<>();
        response.put("id", id);
        return ResponseEntity.ok(response);
    }

}
