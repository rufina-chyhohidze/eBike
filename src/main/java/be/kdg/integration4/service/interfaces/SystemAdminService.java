package be.kdg.integration4.service.interfaces;

import be.kdg.integration4.domain.profile.SystemAdmin;

import java.util.List;

public interface SystemAdminService {
    List<SystemAdmin> getAll();

    SystemAdmin getById(Long id);

    SystemAdmin save(String name, String email, String password);

    void deleteById(Long id);
}
