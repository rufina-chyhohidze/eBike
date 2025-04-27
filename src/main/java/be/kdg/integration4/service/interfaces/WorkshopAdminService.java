package be.kdg.integration4.service.interfaces;

import be.kdg.integration4.domain.enums.UserRole;
import be.kdg.integration4.domain.profile.WorkshopAdmin;
import be.kdg.integration4.domain.report.Workshop;

import java.util.List;

public interface WorkshopAdminService {
    List<WorkshopAdmin> getAll();

    WorkshopAdmin getById(Long id);

    WorkshopAdmin save(String name, String email, String password, UserRole role, Workshop workshop);

    void deleteById(Long id);
}
