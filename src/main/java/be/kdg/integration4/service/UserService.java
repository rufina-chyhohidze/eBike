package be.kdg.integration4.service;


import be.kdg.integration4.domain.User;

import java.util.List;

public interface UserService  {
    List<User> getUnapprovedUsers();
    void approveUser(Long userId);
    void rejectUser(Long userId);
}
