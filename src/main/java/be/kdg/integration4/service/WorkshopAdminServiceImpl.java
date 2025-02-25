package be.kdg.integration4.service;

import be.kdg.integration4.domain.UserRoles;
import be.kdg.integration4.domain.Workshop;
import be.kdg.integration4.domain.WorkshopAdmin;
import be.kdg.integration4.repository.WorkshopAdminRepository;

import java.util.List;

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
    public WorkshopAdmin save(String name, String email, String password, UserRoles role, Workshop workshop) {
        return repository.save(new WorkshopAdmin(name, email, password, role, workshop));
    }

    @Override
    public void deleteById(Long id) {
        repository.deleteById(id);
    }
}
