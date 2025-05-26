package be.kdg.integration4.controller.api;

import be.kdg.integration4.config.security.annotations.TechnicianOnly;
import be.kdg.integration4.controller.api.dtos.BikeDto;
import be.kdg.integration4.service.interfaces.BikeService;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.time.LocalDateTime;

@RestController
@RequestMapping("/api/bikes")
@Slf4j
public class BikesController {

    private final BikeService bikeService;

    public BikesController(BikeService bikeService) {
        this.bikeService = bikeService;
    }

    @PostMapping
    @TechnicianOnly
    // TODO: Shouldn't it be BikeDto?
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
                bikeDto.productionDate(),
                bikeDto.bikeSize(),
                bikeDto.milleage(),
                bikeDto.gearType(),
                bikeDto.engineType(),
                bikeDto.powertrain(),
                bikeDto.accCapacity(),
                bikeDto.maxSupport(),
                bikeDto.enginePowerMax(),
                bikeDto.enginePowerNominal(),
                bikeDto.engineTorque(),
                bikeDto.bikeModelId()
        );

        if (bikeService.getByFrameNumber(bikeDto.frameNumber()).isEmpty()) return ResponseEntity.notFound().build();
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

}
