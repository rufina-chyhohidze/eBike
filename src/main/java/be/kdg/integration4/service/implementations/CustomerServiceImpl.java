package be.kdg.integration4.service.implementations;

import be.kdg.integration4.domain.profile.Customer;
import be.kdg.integration4.domain.enums.UserRole;
import be.kdg.integration4.domain.profile.Technician;
import be.kdg.integration4.domain.report.BikeReport;
import be.kdg.integration4.repository.BikeReportRepository;
import be.kdg.integration4.repository.CustomerRepository;
import be.kdg.integration4.repository.TechnicianRepository;
import be.kdg.integration4.service.interfaces.CustomerService;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CustomerServiceImpl implements CustomerService {

    private final CustomerRepository repository;
    private final PasswordEncoder passwordEncoder;
    private final TechnicianRepository technicianRepository;

    public CustomerServiceImpl(CustomerRepository repository, PasswordEncoder passwordEncoder, TechnicianRepository technicianRepository) {
        this.repository = repository;
        this.passwordEncoder = passwordEncoder;
        this.technicianRepository = technicianRepository;
    }

    @Override
    public List<Customer> getAll() {
        return repository.findAll();
    }

    @Override
    public Customer getById(Long id) {
        return repository.findById(id).orElseThrow();
    }

//    @Override
//    public Customer save(String name, String email, String password, UserRole role, String phoneNumber,) {
//        return repository.save(new Customer(name, email, password, phoneNumber));
//    }

    @Override
    public void deleteById(Long id) {
        repository.deleteById(id);
    }

    @Override
    public Optional<Customer> getByEmailIgnoreCase(String email) {
        return this.repository.findByEmailIgnoreCase(email);
    }

    @Override
    public Optional<Customer> getByNameIgnoreCase(String name) {
        return this.repository.findByNameIgnoreCase(name);
    }

    @Override
    public List<Customer> getAllByNameIgnoreCase(String name) {
        return this.repository.findAllByNameIgnoreCase(name.toLowerCase());

    public Customer updatePhoneNumber(Long loggedInId, Long pathId, String phoneNumber) {
        if (!loggedInId.equals(pathId)) {
            throw new AccessDeniedException("You are not authorized to access this resource.");
        }
        Customer customer = repository.findById(loggedInId).orElseThrow();
        customer.setPhoneNumber(phoneNumber);
        return repository.save(customer);
    }

    @Override
    public Customer updatePassword(Long loggedInId, Long pathId, String password) {
        if (!loggedInId.equals(pathId)) {
            throw new AccessDeniedException("You are not authorized to access this resource.");
        }
        Customer customer = repository.findById(loggedInId).orElseThrow();
        customer.setPassword(passwordEncoder.encode(password));
        return repository.save(customer);
    }
    @Override
    public List<Customer> getByNameIgnoreCase(String name) {
        return this.repository.findByNameIgnoreCase(name.toLowerCase());
    }

    @Override
    public List<Customer> getCustomersByWorkshop(Long workshopId) {
        return repository.findAll()
                .stream()
                .filter(customer ->
                        customer.getRegisteredBy().getWorkshop().getWorkshopId().equals(workshopId)).toList();
    }
}
