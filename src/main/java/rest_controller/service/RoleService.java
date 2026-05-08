package rest_controller.service;


import rest_controller.model.Role;

import java.util.Set;

public interface RoleService {
    void saveRole(Role role);
    Role findByName(String name);
    Role getRoleById(long id);
    Set<Role> getManagedRoles(Set<Role> roles);
}
