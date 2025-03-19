package be.kdg.integration4.service;

import be.kdg.integration4.domain.Customer;
import be.kdg.integration4.domain.UserRole;
import be.kdg.integration4.repository.CustomerRepository;
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
    public List<Customer> findAll() {
        return repository.findAll();
    }

    @Override
    public Customer findById(Long id) {
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
    public Optional<Customer> findByEmailIgnoreCase(String email) {
        return this.repository.findByEmailIgnoreCase(email);
    }
}
