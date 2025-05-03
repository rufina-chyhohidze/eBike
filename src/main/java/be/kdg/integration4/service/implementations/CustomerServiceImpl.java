package be.kdg.integration4.service.implementations;

import be.kdg.integration4.domain.profile.Customer;
import be.kdg.integration4.domain.enums.UserRole;
import be.kdg.integration4.repository.CustomerRepository;
import be.kdg.integration4.service.interfaces.CustomerService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CustomerServiceImpl implements CustomerService {

    private final CustomerRepository repository;

    public CustomerServiceImpl(CustomerRepository repository) {
        this.repository = repository;
    }

    @Override
    public List<Customer> getAll() {
        return repository.findAll();
    }

    @Override
    public Customer getById(Long id) {
        return repository.findById(id).orElseThrow();
    }

    @Override
    public Customer save(String name, String email, String password, UserRole role, String phoneNumber) {
        return repository.save(new Customer(name, email, password, phoneNumber));
    }

    @Override
    public void deleteById(Long id) {
        repository.deleteById(id);
    }

    @Override
    public Optional<Customer> getByEmailIgnoreCase(String email) {
        return this.repository.findByEmailIgnoreCase(email);
    }
}
