package be.kdg.integration4.service;

import be.kdg.integration4.domain.User;
import org.springframework.stereotype.Service;


public interface RegistrationService {
    User createUser(String name,  String email, String password, String role);
}
