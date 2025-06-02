package be.kdg.integration4.controller.api;

import be.kdg.integration4.config.security.annotations.BikeVisibilityCheck;
import be.kdg.integration4.config.security.annotations.TechnicianOnly;
import be.kdg.integration4.controller.api.dtos.BikeDto;
import be.kdg.integration4.controller.api.dtos.BikeSearchTechnicianDto;
import be.kdg.integration4.domain.profile.UserDetailsImpl;
import be.kdg.integration4.domain.report.Bike;
import be.kdg.integration4.service.interfaces.BikeService;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/bikes")
@Slf4j
public class BikesController {

    private final BikeService bikeService;

    public BikesController(BikeService bikeService) {
        this.bikeService = bikeService;
    }

    @GetMapping
    @BikeVisibilityCheck
    public ResponseEntity<List<BikeSearchTechnicianDto>> getBikes(@RequestParam(required = false) String frameNumber,
                                                                  @RequestParam Long customerId) {
        if (frameNumber == null) {
            frameNumber = "";
        }
        List<Bike> bikes = bikeService.getAllAvailableForUserByFrameNumber(customerId,frameNumber);
        return ResponseEntity.ok(bikes.stream().map(bike -> new BikeSearchTechnicianDto(
                bike.getFrameNumber(), bike.getBikeModel().getType(),bike.getBikeModel().getBrand(), bike.getBikeOwner().getName()
                )).toList());
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
