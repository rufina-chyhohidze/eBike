package be.kdg.integration4.service.interfaces;

import be.kdg.integration4.domain.profile.Customer;
import be.kdg.integration4.domain.enums.UserRole;

import java.util.List;
import java.util.Optional;

public interface CustomerService {
    List<Customer> getAll();

    Customer getById(Long id);

    Customer save(String name, String email, String password, UserRole role, String phoneNumber);

    void deleteById(Long id);

    Optional<Customer> getByEmailIgnoreCase(String email);
}
