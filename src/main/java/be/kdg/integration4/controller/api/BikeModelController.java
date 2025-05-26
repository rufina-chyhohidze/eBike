package be.kdg.integration4.controller.api;

import be.kdg.integration4.config.security.annotations.TechnicianOnly;
import be.kdg.integration4.controller.api.dtos.BikeModelOutputDto;
import be.kdg.integration4.domain.report.BikeModel;
import be.kdg.integration4.service.interfaces.BikeService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/bike-models")
public class BikeModelController {
    private final BikeService bikeService;

    public BikeModelController(BikeService bikeService) {
        this.bikeService = bikeService;
    }

    @GetMapping
    @TechnicianOnly
    public ResponseEntity<List<BikeModelOutputDto>> getAllBikeModels() {
        List<BikeModel> bikeModelList = bikeService.getAllBikeModels();
        if(bikeModelList.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(bikeModelList
                .stream()
                .map(model -> new BikeModelOutputDto(
                    model.getId(),
                        model.getType(),
                        model.getBrand(),
                        model.getGearType(),
                        model.getEngineType(),
                        model.getPowertrain(),
                        model.getMaxSupport(),
                        model.getEnginePowerMax(),
                        model.getEnginePowerNominal(),
                        model.getEngineTorque()
                ))
                .toList()
        );
    }

}
