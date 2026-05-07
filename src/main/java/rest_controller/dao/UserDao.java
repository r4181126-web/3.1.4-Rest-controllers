package rest_controller.dao;

import rest_controller.model.User;
import java.util.List;

public interface UserDao {
    void saveUser(User user);
    void removeUserById(long id);
    List<User> getAllUsers();
    void cleanUsersTable();
    void updateUser(User user);
    User getUserById(long id);
    User findByUsername(String username);
}