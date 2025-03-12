package be.kdg.integration4.controller.api;

import be.kdg.integration4.controller.api.dtos.WorkshopMapper;
import be.kdg.integration4.controller.api.dtos.WorkShopDto;
import be.kdg.integration4.domain.Workshop;
import be.kdg.integration4.service.WorkshopService;
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
public class WorkShopController {

    private final WorkshopService workshopService;
    private final WorkshopMapper workshopMapper;

    public WorkShopController(WorkshopService workshopService, WorkshopMapper workshopMapper) {
        this.workshopService = workshopService;
        this.workshopMapper = workshopMapper;
    }

    @GetMapping
    public ResponseEntity<List<WorkShopDto>> getWorkShops() {
        List<Workshop> workshops = this.workshopService.findAll();
        log.info("Found {} workshops", workshops.size());
        if (workshops.isEmpty()) return ResponseEntity.noContent().build();

        return ResponseEntity.ok(workshopMapper.toWorkShopDtoList(workshops));
    }
}
