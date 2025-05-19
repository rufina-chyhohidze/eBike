package be.kdg.integration4.service.interfaces;

import be.kdg.integration4.domain.profile.Customer;
import be.kdg.integration4.domain.enums.UserRole;

import java.util.List;
import java.util.Optional;

public interface CustomerService {
    List<Customer> getAll();

    Customer getById(Long id);


    void deleteById(Long id);

    Optional<Customer> getByEmailIgnoreCase(String email);

    List<Customer> getByNameIgnoreCase(String name);

    List<Customer> getCustomersByWorkshop(Long workshopId);
}
