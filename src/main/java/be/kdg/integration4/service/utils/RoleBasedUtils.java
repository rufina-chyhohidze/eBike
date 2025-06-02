package be.kdg.integration4.service.utils;

import be.kdg.integration4.domain.profile.Customer;
import be.kdg.integration4.domain.profile.Technician;
import be.kdg.integration4.domain.profile.User;
import be.kdg.integration4.domain.profile.WorkshopAdmin;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class RoleBasedUtils {
    public static List<User> filterUsersForTechnician(User loggedInUser, List<User> users) {
        return users.stream()
                .filter(user ->
                        user instanceof Customer customer &&
                                customer.getRegisteredIn().equals(((Technician) loggedInUser).getWorkshop())
                ).toList();
    }

    public static List<User> filterUsersForWorkshopAdmin(User loggedInUser, List<User> users) {
        var workshop = ((WorkshopAdmin) loggedInUser).getWorkshop();
        return users.stream()
                .filter(user ->
                        (user instanceof Customer customer &&
                                customer.getRegisteredIn().equals(workshop))
                                ||
                                (user instanceof Technician technician &&
                                        technician.getWorkshop().equals(workshop))
                ).toList();
    }

}
