package be.kdg.integration4.controller.api;

import be.kdg.integration4.controller.api.dtos.WorkShopDto;
import be.kdg.integration4.controller.api.dtos.mappers.WorkshopDtoMapper;
import be.kdg.integration4.domain.report.Workshop;
import be.kdg.integration4.service.interfaces.WorkshopService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

//@CrossOrigin(origins = "http://localhost:63342")  // Allow requests from your frontend
@Slf4j
@RestController
@RequestMapping("/api/workshops")
public class WorkshopController {

    private final WorkshopService workshopService;
    private final WorkshopDtoMapper customMapper;

    public WorkshopController(WorkshopService workshopService, WorkshopDtoMapper customMapper) {
        this.workshopService = workshopService;
        this.customMapper = customMapper;
      //  this.workshopDtoMapper = workshopDtoMapper;
    }

    @GetMapping
    public ResponseEntity<List<WorkShopDto>> getWorkShops() {
        List<Workshop> workshops = this.workshopService.getAll();
        log.info("Found {} workshops", workshops.size());
        if (workshops.isEmpty()) return ResponseEntity.noContent().build();

        return ResponseEntity.ok(customMapper.toWorkShopDtoList(workshops));
    }
}
