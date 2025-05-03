package be.kdg.integration4.service.implementations;

import be.kdg.integration4.domain.profile.Technician;
import be.kdg.integration4.domain.report.ReportSetting;
import be.kdg.integration4.repository.ReportSettingRepository;
import be.kdg.integration4.service.interfaces.ReportSettingService;
import be.kdg.integration4.service.interfaces.TechnicianService;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Transactional
@Service
public class ReportSettingServiceImpl implements ReportSettingService {

    @Autowired
    private ReportSettingRepository reportSettingRepository;

    @Autowired
    private TechnicianService technicianService; // Service to get the logged-in technician


    @Override
    public ReportSetting getReportSettingsForTechnician(Technician technician) {
        // Assume you're getting the currently logged-in technician from some service

        return reportSettingRepository.findByTechnician(technician)
                .orElseGet(() -> new ReportSetting(technician)); // If no settings, create a new one
    }

    @Override
    public boolean updateSetting(String settingName, Double settingValue, Technician technician) {
        // Retrieve the current settings for the logged-in technician
        ReportSetting reportSetting = reportSettingRepository.findByTechnician(technician)
                .orElseThrow(() -> new RuntimeException("Settings not found"));

        // Update the setting
        if ("horizontalVibration".equals(settingName)) {
            reportSetting.setHorizontalVibration(settingValue);
        } else if ("verticalVibration".equals(settingName)) {
            reportSetting.setVerticalVibration(settingValue);
        } else {
            return false; // If invalid setting name
        }

        reportSettingRepository.save(reportSetting); // Save the updated settings
        return true;
    }
}
