package be.kdg.integration4.service.interfaces;

import be.kdg.integration4.domain.profile.User;
import be.kdg.integration4.service.dtos.CustomerAndPasswordServiceDto;


public interface RegistrationService {
    CustomerAndPasswordServiceDto createCustomer(String name, String email, String phoneNumber);
    User createStaff(String name, String email, String password, String role, Long workshopId);
}
