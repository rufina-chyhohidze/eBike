package be.kdg.integration4.service;

import be.kdg.integration4.domain.Technician;
import be.kdg.integration4.domain.UserRole;
import be.kdg.integration4.domain.Workshop;
import be.kdg.integration4.repository.TechnicianRepository;

import java.util.List;

public class TechnicianServiceImpl implements TechnicianService {
    TechnicianRepository technicianRepository;

    @Override
    public Technician findById(Long id){
        return technicianRepository.findById(id).orElse(null);
    }

    @Override
    public List<Technician> findAll(){
        return technicianRepository.findAll();
    }

    @Override
    public void save(Long id, String name, String email, String password, UserRole userRole, Workshop workshop) {
    }

    @Override
    public void delete(Long id) {
    }
}
