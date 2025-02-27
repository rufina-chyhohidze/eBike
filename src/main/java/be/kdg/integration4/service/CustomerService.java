package be.kdg.integration4.service;

import be.kdg.integration4.domain.Customer;
import be.kdg.integration4.domain.UserRoles;

import java.util.List;

public interface CustomerService {
    List<Customer> findAll();

    Customer findById(Long id);

    Customer save(String name, String email, String password, UserRoles role, String phoneNumber);

    void deleteById(Long id);
}
