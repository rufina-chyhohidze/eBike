package be.kdg.integration4.service;

import be.kdg.integration4.domain.SystemAdmin;

import java.util.List;

public interface SystemAdminService {
    List<SystemAdmin> findAll();

    SystemAdmin findById(Long id);

    SystemAdmin save(String name, String email, String password);

    void deleteById(Long id);
}
