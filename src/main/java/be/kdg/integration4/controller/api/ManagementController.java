package be.kdg.integration4.controller.api;

import be.kdg.integration4.config.security.annotations.CustomerOnly;
import be.kdg.integration4.domain.profile.UserDetailsImpl;
import be.kdg.integration4.service.interfaces.BikeService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/bike-management")
public class ManagementController {

    BikeService bikeService;

    public ManagementController(BikeService bikeService) {
        this.bikeService = bikeService;
    }


    @PatchMapping("/{frameNumber}/unlink")
    @CustomerOnly
    public ResponseEntity<Void> unlinkBike(@PathVariable String frameNumber,
                                           @AuthenticationPrincipal UserDetailsImpl customer) {
        bikeService.unlinkBikeFromCustomer(frameNumber, customer.getUserId());
        return ResponseEntity.noContent().build();
    }// 204 No Content
}
