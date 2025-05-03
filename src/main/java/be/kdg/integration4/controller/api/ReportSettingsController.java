package be.kdg.integration4.controller.api;

import be.kdg.integration4.controller.api.dtos.UpdateSettingsDto;
import be.kdg.integration4.domain.profile.Technician;
import be.kdg.integration4.service.interfaces.ReportSettingService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/report-settings")
public class ReportSettingsController {
    private final ReportSettingService reportSettingService;

    public ReportSettingsController(ReportSettingService reportSettingService) {
        this.reportSettingService = reportSettingService;
    }

    @PatchMapping("")
    public ResponseEntity<Void> updateReportSetting(
            @RequestBody @Valid UpdateSettingsDto settingsDto,
            @AuthenticationPrincipal Technician user) {


        boolean success = reportSettingService.updateSetting(
                settingsDto.settingName(), settingsDto.settingValue(), user);

        System.out.println("before.2");

        if (success) {
            System.out.println("worked");
            return ResponseEntity.noContent().build();
        } else {
            System.out.println("didn't work");
            return ResponseEntity.notFound().build();
        }
    }

}
