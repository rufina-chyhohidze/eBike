package be.kdg.integration4.service.implementations;

import be.kdg.integration4.domain.UserRole;
import be.kdg.integration4.domain.Workshop;
import be.kdg.integration4.domain.WorkshopAdmin;
import be.kdg.integration4.repository.WorkshopAdminRepository;
import be.kdg.integration4.service.interfaces.WorkshopAdminService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class WorkshopAdminServiceImpl implements WorkshopAdminService {

    private final WorkshopAdminRepository repository;

    public WorkshopAdminServiceImpl(WorkshopAdminRepository repository) {
        this.repository = repository;
    }


    @Override
    public List<WorkshopAdmin> findAll() {
        return repository.findAll();
    }

    @Override
    public WorkshopAdmin findById(Long id) {
        return repository.findById(id).orElseThrow();
    }

    @Override
    public WorkshopAdmin save(String name, String email, String password, UserRole role, Workshop workshop) {
        return repository.save(new WorkshopAdmin(name, email, password, workshop));
    }

    @Override
    public void deleteById(Long id) {
        repository.deleteById(id);
    }
}
