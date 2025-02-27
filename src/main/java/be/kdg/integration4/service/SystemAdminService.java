package be.kdg.integration4.service;

import be.kdg.integration4.domain.Customer;
import be.kdg.integration4.domain.SystemAdmin;
import be.kdg.integration4.domain.UserRoles;
import be.kdg.integration4.domain.Workshop;

import java.util.List;

public interface SystemAdminService {
    List<SystemAdmin> findAll();

    SystemAdmin findById(Long id);

    SystemAdmin save(String name, String email, String password, UserRoles role);

    void deleteById(Long id);
}
