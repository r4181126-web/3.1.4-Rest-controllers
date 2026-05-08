package rest_controller.service;


import org.springframework.stereotype.Service;
import rest_controller.dao.RoleDao;
import rest_controller.model.Role;

import java.util.HashSet;
import java.util.Set;

@Service
public class RoleServiceImpl implements RoleService {
    private final RoleDao roleDao;

    public RoleServiceImpl(RoleDao roleDao) {
        this.roleDao = roleDao;
    }

    @Override
    public void saveRole(Role role) {
        roleDao.saveRole(role);
    }

    @Override
    public Role findByName(String name) {
        return roleDao.findByName(name);
    }

    @Override
    public Role getRoleById(long id) { return roleDao.getRoleById(id); }

    @Override
    public Set<Role> getManagedRoles(Set<Role> roles) {
        Set<Role> managedRoles = new HashSet<>();
        if(roles != null) {
            for (Role role : roles) {
                Role existingRole = roleDao.getRoleById(role.getId());
                if (existingRole != null) {
                    managedRoles.add(existingRole);
                }
            }
        }
        return Set.of();
    }
}
