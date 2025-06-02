package be.kdg.integration4.service.interfaces;


import be.kdg.integration4.domain.profile.UserDetailsImpl;

public interface AuthorizationService {
    boolean canSeeCustomerBikes(UserDetailsImpl user, Long customerId);
}
