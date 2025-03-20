package be.kdg.integration4.service.implementations;

import be.kdg.integration4.domain.profile.SystemAdmin;
import be.kdg.integration4.repository.SystemAdminRepository;
import be.kdg.integration4.service.interfaces.SystemAdminService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SystemAdminServiceImpl  implements SystemAdminService {

    private final SystemAdminRepository repository;

    public SystemAdminServiceImpl(SystemAdminRepository repository) {
        this.repository = repository;
    }


    @Override
    public List<SystemAdmin> findAll() {
        return repository.findAll();
    }

    @Override
    public SystemAdmin findById(Long id) {
        return repository.findById(id).orElseThrow();
    }

    @Override
    public SystemAdmin save(String name, String email, String password) {
        return repository.save(new SystemAdmin(name, email, password));
    }

    @Override
    public void deleteById(Long id) {
        repository.deleteById(id);
    }
}
