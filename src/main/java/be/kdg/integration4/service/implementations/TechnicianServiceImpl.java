package be.kdg.integration4.service.implementations;

import be.kdg.integration4.domain.profile.Technician;
import be.kdg.integration4.domain.enums.UserRole;
import be.kdg.integration4.domain.report.Workshop;
import be.kdg.integration4.repository.BikeReportRepository;
import be.kdg.integration4.repository.TechnicianRepository;
import be.kdg.integration4.service.interfaces.TechnicianService;
import org.springframework.stereotype.Service;
import be.kdg.integration4.repository.UserRepository;

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
    public Technician getById(Long id){
        return userRepository.findTechnicianById(id).orElse(null);
    }

    @Override
    public List<Technician> getAll(){
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
    public Technician getByEmail(String email) {
        return technicianRepository.findByEmail(email).orElseThrow();
    }
}
