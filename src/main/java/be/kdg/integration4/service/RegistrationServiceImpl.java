package be.kdg.integration4.service;

import be.kdg.integration4.domain.*;
import be.kdg.integration4.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class RegistrationServiceImpl implements RegistrationService {

    private final CustomerRepository customerRepository;
    private final WorkshopAdminRepository workshopAdminRepository;
    private final TechnicianRepository technicianRepository;
    private final PasswordEncoder passwordEncoder;

    public RegistrationServiceImpl(CustomerRepository customerRepository, WorkshopAdminRepository workshopAdminRepository, TechnicianRepository technicianRepository, PasswordEncoder passwordEncoder) {
        this.customerRepository = customerRepository;
        this.workshopAdminRepository = workshopAdminRepository;
        this.technicianRepository = technicianRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public User createUser(String name, String email, String password, String role) {
        if (role.equalsIgnoreCase("CUSTOMER")) {
            return customerRepository.save(new Customer(name, email, passwordEncoder.encode(password), "123456789"));
        } else if (role.equalsIgnoreCase("TECHNICIAN")) {
            return technicianRepository.save(new Technician(name, email, passwordEncoder.encode(password), new Workshop("workshop", Location.ANTWERP)));
        } else if (role.equalsIgnoreCase("WORKSHOP_ADMIN")) {
            return workshopAdminRepository.save(new WorkshopAdmin(name, email, passwordEncoder.encode(password), new Workshop("workshop", Location.ANTWERP)));
        } else return null;
    }
}
