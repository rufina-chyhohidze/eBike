package be.kdg.integration4.service.interfaces;

import be.kdg.integration4.domain.profile.Technician;
import be.kdg.integration4.domain.report.ReportSetting;

public interface ReportSettingService {
    ReportSetting getReportSettingsForTechnician(Technician technician);

    boolean updateSetting(String settingName, Double settingValue, Technician technician);
}
