package be.kdg.integration4.service.interfaces;

import be.kdg.integration4.domain.profile.Technician;
import be.kdg.integration4.domain.enums.UserRole;
import be.kdg.integration4.domain.report.Workshop;

import java.util.List;

public interface TechnicianService {
    Technician getById(Long id);

    List<Technician> getAll();

    void save(Long id, String name, String email, String password, UserRole userRole, Workshop workshop);

    void delete(Long id);

    int getTotalReportsByTechnician(long technicianId);

    Technician getByEmail(String email);
}
