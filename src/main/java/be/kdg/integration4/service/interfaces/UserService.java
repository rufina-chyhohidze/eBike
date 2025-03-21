package be.kdg.integration4.service.interfaces;


import be.kdg.integration4.domain.profile.User;

import java.util.List;

public interface UserService  {
    List<User> getUnapprovedUsers();
    void approveUser(Long userId);
    void rejectUser(Long userId);

    User getUserByEmail(String email);
}
