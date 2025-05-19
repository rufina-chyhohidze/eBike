package be.kdg.integration4.service.interfaces;


import be.kdg.integration4.domain.profile.User;
import be.kdg.integration4.domain.profile.UserDetailsImpl;

import java.util.List;

public interface UserService  {
    List<User> getUnapprovedUsers();
    void approveUser(Long userId);
    void rejectUser(Long userId);
    User getUserById(Long userId);
    User getUserByEmail(String email);
    List<User> getAll();
    List<User> getAllWithoutLoggedInUser(Long loggedInUser);
    List<User> getAllFilteredByName(String name);
    User updatePassword(Long userId,Long loggedInUserId, String password);
}
