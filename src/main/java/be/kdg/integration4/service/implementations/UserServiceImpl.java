package be.kdg.integration4.service.implementations;

import be.kdg.integration4.domain.User;
import be.kdg.integration4.repository.UserRepository;
import be.kdg.integration4.service.interfaces.UserService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
//@Slf4j
public class UserServiceImpl implements UserService {
    private final Logger log = LoggerFactory.getLogger(UserServiceImpl.class);
    private final UserRepository userRepository;

    public List<User> getUnapprovedUsers() {
        return userRepository.findUnapprovedUsers();
    }

    public void approveUser(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));
        user.setApproved(true);
        userRepository.save(user);
        log.debug("User {} approved", user.getEmail());
    }

    public void rejectUser(Long userId) {
        userRepository.deleteById(userId);
        log.debug("User with ID {} rejected and removed", userId);
    }

    @Override
    public User getUserByEmail(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with email: " + email));
    }
}
