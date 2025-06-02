package be.kdg.integration4.service.implementations;

import be.kdg.integration4.domain.profile.User;
import be.kdg.integration4.domain.profile.UserDetailsImpl;
import be.kdg.integration4.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

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
                .orElseThrow(() -> new UsernameNotFoundException("User not found with email: " + email));

        if (!user.isApproved()) {
            log.warn("User {} attempted to login but is not approved yet.", email);
            throw new DisabledException("Account not approved yet");
        }

        return new UserDetailsImpl(
                user.getEmail(),
                user.getPassword(),
                List.of(new SimpleGrantedAuthority("ROLE_" + user.getClass().getSimpleName().toUpperCase())),
                user.getId()
        );
    }

}
