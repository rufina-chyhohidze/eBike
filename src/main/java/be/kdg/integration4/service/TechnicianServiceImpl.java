package be.kdg.integration4.service;

import be.kdg.integration4.domain.Technician;
import be.kdg.integration4.domain.UserRole;
import be.kdg.integration4.domain.Workshop;
import be.kdg.integration4.repository.BikeReportRepository;
import be.kdg.integration4.repository.TechnicianRepository;
import be.kdg.integration4.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TechnicianServiceImpl implements TechnicianService {
    TechnicianRepository technicianRepository;
    BikeReportRepository bikeReportRepository;
    UserRepository userRepository;

    public TechnicianServiceImpl(TechnicianRepository technicianRepository, BikeReportRepository bikeReportRepository, UserRepository userRepository) {
        this.technicianRepository = technicianRepository;
        this.bikeReportRepository = bikeReportRepository;
        this.userRepository = userRepository;
    }

    @Override
    public Technician findById(Long id){
        return userRepository.findTechnicianById(id).orElse(null);
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

    @Override
    public int getTotalReportsByTechnician(long technicianId) {
        return bikeReportRepository.countByTechnicianId(technicianId);
    }

    @Override
    public Technician findByEmail(String email) {
        return technicianRepository.findByEmail(email);
    }
}
