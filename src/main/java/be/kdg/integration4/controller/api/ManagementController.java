package be.kdg.integration4.controller.api;

import be.kdg.integration4.config.security.annotations.CustomerOnly;
import be.kdg.integration4.domain.profile.UserDetailsImpl;
import be.kdg.integration4.service.interfaces.BikeService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/bike-management")
public class ManagementController {

    BikeService bikeService;

    public ManagementController(BikeService bikeService) {
        this.bikeService = bikeService;
    }


    @DeleteMapping("/{frameNumber}")
    public ResponseEntity<Void> deleteBike(@PathVariable String frameNumber) {
        bikeService.deleteBike(frameNumber);
        return ResponseEntity.noContent().build();
    }

}
