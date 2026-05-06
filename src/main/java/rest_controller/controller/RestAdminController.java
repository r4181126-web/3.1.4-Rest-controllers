package rest_controller.controller;

import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import rest_controller.dao.RoleDao;
import rest_controller.model.Role;
import rest_controller.model.User;
import rest_controller.service.UserService;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@RestController
@RequestMapping("/api/admin")
public class RestAdminController {
    private final UserService userService;
    private final RoleDao roleDao;

    public RestAdminController(UserService userService, RoleDao roleDao) {
        this.userService = userService;
        this.roleDao = roleDao;
    }
    @GetMapping("/users")
    public List<User> getAllUsers() {
        return userService.getAllUsers();
    }
    @GetMapping("/users/{id}")
    public User getUserById(@PathVariable("id") long id) {
        return userService.getUserById(id);
    }
    @PostMapping("/users")
    public void addUser(@RequestBody User user) {
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
        userService.saveUser(user);
    }
    @PutMapping("/users/{id}")
    public void updateUser(@PathVariable("id") long id, @RequestBody User user) {
        user.setId(id);
        if (user.getPassword() == null || user.getPassword().isEmpty()) {
            User existingUser = userService.getUserById(id);
            user.setPassword(existingUser.getPassword());
        }
        Set<Role> managedRoles = new HashSet<>();
        for (Role role : user.getRoles()) {
            Role existingRole = roleDao.getRoleById(role.getId());
            if (existingRole != null) {
                managedRoles.add(existingRole);
            }
        }
        user.setRoles(managedRoles);
        userService.updateUser(user);
    }
    @DeleteMapping("/users/{id}")
    public void deleteUser(@PathVariable("id") long id) {
        userService.deleteUser(id);
    }
    @GetMapping("/current-user")
    public User getCurrentUser(Authentication authentication) {
        return userService.findByUsername(authentication.getName());
    }
}
