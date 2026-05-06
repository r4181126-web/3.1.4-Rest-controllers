package rest_controller.service;


import org.springframework.security.core.userdetails.UserDetailsService;
import rest_controller.model.User;

import java.util.List;

public interface UserService extends UserDetailsService {
    List<User> getAllUsers();
    User getUserById(long id);
    void saveUser(User user);
    void updateUser(User user);
    void deleteUser(long id);
    User findByUsername(String username);
}