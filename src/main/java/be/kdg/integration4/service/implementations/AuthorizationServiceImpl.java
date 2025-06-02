package be.kdg.integration4.service.implementations;

import be.kdg.integration4.domain.enums.UserRole;
import be.kdg.integration4.domain.profile.*;
import be.kdg.integration4.repository.CustomerRepository;
import be.kdg.integration4.repository.UserRepository;
import be.kdg.integration4.service.interfaces.AuthorizationService;
import org.springframework.stereotype.Service;

@Service("authorizationService")
public class AuthorizationServiceImpl implements AuthorizationService {

    private final UserRepository userRepository;
    private final CustomerRepository customerRepository;

    public AuthorizationServiceImpl(UserRepository userRepository, CustomerRepository customerRepository) {
        this.userRepository = userRepository;
        this.customerRepository = customerRepository;
    }

    @Override
    public boolean canSeeCustomerBikes(UserDetailsImpl user, Long customerId) {
        User appUser = userRepository.findById(user.getUserId()).orElseThrow();
        Customer customer = customerRepository.findById(customerId).orElseThrow();
        UserRole role = UserRole.valueOf(appUser.getClass().getSimpleName().toUpperCase());

        return switch (role) {
            case CUSTOMER -> customerId.equals(appUser.getId());
            case TECHNICIAN ->
                    customer.getRegisteredIn().getWorkshopId().equals(((Technician) appUser).getWorkshop().getWorkshopId());
            case WORKSHOPADMIN ->
                    customer.getRegisteredIn().getWorkshopId().equals(((WorkshopAdmin) appUser).getWorkshop().getWorkshopId());
            case SYSTEMADMIN -> true;
        };
    }
}
