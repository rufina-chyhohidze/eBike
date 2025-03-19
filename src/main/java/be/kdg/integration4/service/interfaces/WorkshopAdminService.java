package be.kdg.integration4.service.interfaces;

import be.kdg.integration4.domain.*;

import java.util.List;

public interface WorkshopAdminService {
    List<WorkshopAdmin> findAll();

    WorkshopAdmin findById(Long id);

    WorkshopAdmin save(String name, String email, String password, UserRole role, Workshop workshop);

    void deleteById(Long id);
}
