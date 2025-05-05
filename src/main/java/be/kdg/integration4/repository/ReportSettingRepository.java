package be.kdg.integration4.repository;

import be.kdg.integration4.domain.report.ReportSetting;
import be.kdg.integration4.domain.profile.Technician;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ReportSettingRepository extends JpaRepository<ReportSetting, Long> {

    Optional<ReportSetting> findByTechnician(Technician technician);
}
