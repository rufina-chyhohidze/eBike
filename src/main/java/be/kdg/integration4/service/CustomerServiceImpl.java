package be.kdg.integration4.service;

import be.kdg.integration4.domain.Customer;
import be.kdg.integration4.domain.UserRoles;
import be.kdg.integration4.repository.CustomerRepository;

import java.util.List;

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
    public Customer save(String name, String email, String password, UserRoles role, String phoneNumber) {
        return repository.save(new Customer(name, email, password, phoneNumber));
    }

    @Override
    public void deleteById(Long id) {
        repository.deleteById(id);
    }
}
