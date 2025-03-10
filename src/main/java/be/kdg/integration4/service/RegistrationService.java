package be.kdg.integration4.service;

import be.kdg.integration4.domain.Customer;
import be.kdg.integration4.domain.User;
import be.kdg.integration4.domain.Workshop;


public interface RegistrationService {
    Customer createCustomer(String name, String email, String password, String phoneNumber);
    User createStaff(String name, String email, String password, String role, Long workshopId);
}
