package be.kdg.integration4.controller.api;

import be.kdg.integration4.config.security.SecurityUtil;
import be.kdg.integration4.controller.api.dtos.UpdateSettingsDto;
import be.kdg.integration4.domain.profile.Technician;
import be.kdg.integration4.repository.CustomerRepository;
import be.kdg.integration4.repository.TechnicianRepository;
import be.kdg.integration4.service.interfaces.ReportSettingService;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/report-settings")
public class ReportSettingsController {
    private final ReportSettingService reportSettingService;
    private final TechnicianRepository technicianRepository;

    public ReportSettingsController(ReportSettingService reportSettingService, TechnicianRepository technicianRepository) {
        this.reportSettingService = reportSettingService;
        this.technicianRepository = technicianRepository;
    }

    @PatchMapping("")
    public ResponseEntity<Void> updateReportSetting(@RequestBody List<UpdateSettingsDto> settingsDtos) {

        Technician user = technicianRepository.findByEmail(SecurityUtil.getLoggedInUsername());

        if (user == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        for (UpdateSettingsDto settingsDto : settingsDtos) {
            boolean success = reportSettingService.updateSetting(
                    settingsDto.settingName(), settingsDto.settingValue(), user);

            if (!success) {
                return ResponseEntity.notFound().build();
            }
        }

        return ResponseEntity.ok().build();
    }

}
