package be.kdg.integration4.service.interfaces;

import be.kdg.integration4.domain.profile.Customer;
import be.kdg.integration4.domain.profile.User;


public interface RegistrationService {
    Customer createCustomer(String name, String email, String password, String phoneNumber);
    User createStaff(String name, String email, String password, String role, Long workshopId);
}
