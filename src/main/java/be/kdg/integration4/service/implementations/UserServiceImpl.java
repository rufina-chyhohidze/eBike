package be.kdg.integration4.service.implementations;

import be.kdg.integration4.domain.profile.Technician;
import be.kdg.integration4.domain.profile.User;
import be.kdg.integration4.domain.report.ReportSetting;
import be.kdg.integration4.repository.ReportSettingRepository;
import be.kdg.integration4.domain.enums.UserRole;
import be.kdg.integration4.domain.profile.*;
import be.kdg.integration4.repository.UserRepository;
import be.kdg.integration4.service.interfaces.UserService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.parameters.P;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

import static be.kdg.integration4.service.utils.RoleBasedUtils.filterUsersForTechnician;
import static be.kdg.integration4.service.utils.RoleBasedUtils.filterUsersForWorkshopAdmin;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final Logger log = LoggerFactory.getLogger(UserServiceImpl.class);
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final ReportSettingRepository reportSettingRepository;

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

    @Override
    public List<User> getAll() {
        return userRepository.findAll();
    }

    @Override
    public List<User> getAllWithoutLoggedInUser(Long loggedInUser) {
        List<User> users = userRepository.findAll();
        users.removeIf(user -> user.getId().equals(loggedInUser));
        return users;
    }

    public List<User> getAllFilteredByName(String name) {
        return userRepository.findAllByNameContainsIgnoreCase(name);
    }

    @Override
    public User updatePassword(Long userId, Long loggedInUserId, String password) {
        if (!loggedInUserId.equals(userId)) {
            throw new AccessDeniedException("You are not authorized to access this resource.");
        }
        User customer = userRepository.findById(loggedInUserId).orElseThrow();
        customer.setPassword(passwordEncoder.encode(password));
        return userRepository.save(customer);

    }

    @Override
    public void delete(Long id) {
        User user = userRepository.findById(id).orElseThrow();
        if (user instanceof Technician) {
            ReportSetting setting = reportSettingRepository.findByTechnician((Technician) user).orElseThrow();
            reportSettingRepository.delete(setting);
        }
        userRepository.delete(user);
    }

    @Override
    public List<User> getAvailableWithoutLoggedInUser(UserDetailsImpl principal) {
        return this.getAvailableUsersHelper(principal, null);
    }

    @Override
    public List<User> getAvailableFilteredByName(String name, UserDetailsImpl principal) {
        return this.getAvailableUsersHelper(principal, name);
    }

    private List<User> getAvailableUsersHelper(UserDetailsImpl principal, String name) {
        Long loggedInUserId = principal.getUserId();
        User loggedInUser = userRepository.findById(loggedInUserId).orElseThrow();
        UserRole role = UserRole.valueOf(loggedInUser.getClass().getSimpleName().toUpperCase());

        List<User> users = new ArrayList<>();
        users.addAll(userRepository.findAllCustomersWithTechnicianAndWorkshop());
        users.addAll(userRepository.findAllTechniciansWithWorkshop());
        users.addAll(userRepository.findAllWorkshopAdminsWithWorkshop());

        if (name != null) {
            users = users.stream().filter(user -> user.getName().toLowerCase().contains(name.toLowerCase())).toList();
        } else {
            users.removeIf(u -> u.getId().equals(loggedInUserId));
        }
        return switch (role) {
            case SYSTEMADMIN -> users;
            case TECHNICIAN -> filterUsersForTechnician(loggedInUser, users);
            case WORKSHOPADMIN -> filterUsersForWorkshopAdmin(loggedInUser, users);
            default -> throw new AccessDeniedException("You are not authorized to access this resource.");
        };
    }

    @Override
    public User getUserById(Long id) {
        return userRepository.findById(id).orElseThrow(() -> new UsernameNotFoundException("User not found with id: " + id));
    }
}