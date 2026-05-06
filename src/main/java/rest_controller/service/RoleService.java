package rest_controller.service;


import rest_controller.model.Role;

public interface RoleService {
    void saveRole(Role role);
    Role findByName(String name);
}
