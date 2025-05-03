package be.kdg.integration4.service.implementations;

import be.kdg.integration4.domain.enums.UserRole;
import be.kdg.integration4.domain.profile.Customer;
import be.kdg.integration4.domain.profile.Technician;
import be.kdg.integration4.domain.profile.User;
import be.kdg.integration4.domain.profile.WorkshopAdmin;
import be.kdg.integration4.domain.report.Workshop;
import be.kdg.integration4.exception.UserAlreadyExistsException;
import be.kdg.integration4.repository.*;
import be.kdg.integration4.service.dtos.CustomerAndPasswordServiceDto;
import be.kdg.integration4.service.interfaces.RegistrationService;
import be.kdg.integration4.service.utils.PasswordGenerationUtil;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.NoSuchElementException;

@Service
public class RegistrationServiceImpl implements RegistrationService {

    private final CustomerRepository customerRepository;
    private final WorkshopAdminRepository workshopAdminRepository;
    private final TechnicianRepository technicianRepository;
    private final WorkshopRepository workshopRepository;
    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;

    public RegistrationServiceImpl(CustomerRepository customerRepository, WorkshopAdminRepository workshopAdminRepository, TechnicianRepository technicianRepository, WorkshopRepository workshopRepository, PasswordEncoder passwordEncoder, UserRepository userRepository) {
        this.customerRepository = customerRepository;
        this.workshopAdminRepository = workshopAdminRepository;
        this.technicianRepository = technicianRepository;
        this.workshopRepository = workshopRepository;
        this.passwordEncoder = passwordEncoder;
        this.userRepository = userRepository;
    }

    private void checkIfUserExists(String email) {
        if (userRepository.findByEmail(email).isPresent()) {
            throw new UserAlreadyExistsException("User with this email already exists");
        }
    }


    @Override
    public CustomerAndPasswordServiceDto createCustomer(String name, String email, String phoneNumber) {
        this.checkIfUserExists(email);
        String password = PasswordGenerationUtil.generatePassword(12);
        return new CustomerAndPasswordServiceDto(customerRepository.save(new Customer(name, email,
                passwordEncoder.encode(password), phoneNumber)), password);
    }

    @Override
    public User createStaff(String name, String email, String password, String role, Long workshopId) {
        this.checkIfUserExists(email);

        Workshop workshop = workshopRepository.findById(workshopId)
                .orElseThrow(() -> new NoSuchElementException("Workshop not found"));

        UserRole userRole;
        try {
            userRole = UserRole.valueOf(role.toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Invalid role: " + role);
        }

        return switch (userRole) {
            case WORSHOPADMIN -> workshopAdminRepository.save(new WorkshopAdmin(name, email, passwordEncoder.encode(password), workshop));
            case TECHNICIAN -> technicianRepository.save(new Technician(name, email, passwordEncoder.encode(password), workshop));
            default -> throw new IllegalArgumentException("Invalid role: " + role);
        };
    }
}
