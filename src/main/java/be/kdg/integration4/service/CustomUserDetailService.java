package be.kdg.integration4.service;

import be.kdg.integration4.domain.Technician;
import be.kdg.integration4.domain.WorkshopAdmin;
import be.kdg.integration4.domain.User;
import be.kdg.integration4.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class CustomUserDetailService implements UserDetailsService {
    private final UserRepository userRepository;

    public CustomUserDetailService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException(email));

        // Only WorkshopAdmin and Technician require approval
        if (!user.isApproved() && (user instanceof WorkshopAdmin || user instanceof Technician)) {
            throw new DisabledException("Account not approved yet");
        }

        return user;
    }

}
