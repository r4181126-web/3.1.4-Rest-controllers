package rest_controller.service;


import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import rest_controller.dao.RoleDao;
import rest_controller.dao.UserDao;
import rest_controller.model.Role;
import rest_controller.model.User;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
public class UserServiceImpl implements UserService {
    private final UserDao userDao;
    private final PasswordEncoder passwordEncoder;
    private final RoleDao roleDao;

    public UserServiceImpl(UserDao userDao, PasswordEncoder passwordEncoder, RoleDao roleDao) {
        this.userDao = userDao;
        this.passwordEncoder = passwordEncoder;
        this.roleDao = roleDao;
    }

    // UserDetailsService
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userDao.findByUsername(username);
        if (user == null) {
            throw new UsernameNotFoundException("User not found: " + username);
        }
        return user;
    }

    @Override
    public List<User> getAllUsers() {
        return userDao.getAllUsers();
    }

    @Override
    public User getUserById(long id) {
        return userDao.getUserById(id);
    }

    @Override
    @Transactional
    public void saveUser(User user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        Set<Role> managedRoles = new HashSet<>();
        if (user.getRoles() != null) {
            for (Role role : user.getRoles()) {
                Role existingRole = roleDao.getRoleById(role.getId());
                if (existingRole != null) {
                    managedRoles.add(existingRole);
                }
            }
        }
        user.setRoles(managedRoles);
        userDao.saveUser(user);
    }

    @Override
    @Transactional
    public void updateUser(User user) {
        User existingUser = userDao.getUserById(user.getId());
        if (user.getPassword() != null && !user.getPassword().isEmpty()
                && !user.getPassword().startsWith("$2a$")) {
            user.setPassword(passwordEncoder.encode(user.getPassword()));
        } else {
            user.setPassword(existingUser.getPassword());
        }
        Set<Role> managedRoles = new HashSet<>();
        if (user.getRoles() != null) {
            for (Role role : user.getRoles()) {
                Role existingRole = roleDao.getRoleById(role.getId());
                if (existingRole != null) {
                    managedRoles.add(existingRole);
                }
            }
        }
        user.setRoles(managedRoles);
        userDao.updateUser(user);
    }

    @Override
    @Transactional
    public void deleteUser(long id) {
        userDao.removeUserById(id);
    }

    @Override
    public User findByUsername(String username) {
        return userDao.findByUsername(username);
    }
}