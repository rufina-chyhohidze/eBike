package be.kdg.integration4.service;

import be.kdg.integration4.domain.BikeReport;
import be.kdg.integration4.domain.Technician;
import be.kdg.integration4.domain.UserRoles;
import be.kdg.integration4.domain.Workshop;

import java.util.List;

public interface TechnicianService {
    Technician findById(Long id);

    List<Technician> findAll();

    void save(Long id, String name, String email, String password, UserRoles userRoles, Workshop workshop);

    void delete(Long id);

}
