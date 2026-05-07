package rest_controller.service;


import org.springframework.stereotype.Service;
import rest_controller.dao.RoleDao;
import rest_controller.model.Role;

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
}
