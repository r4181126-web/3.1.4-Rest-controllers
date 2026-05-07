package rest_controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import rest_controller.dao.RoleDao;
import rest_controller.model.Role;
import rest_controller.service.RoleServiceImpl;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class RoleServiceTest {
    @Mock
    private RoleDao roleDao;
    @InjectMocks
    private RoleServiceImpl roleServiceImpl;

    private Role adminRole;
    private Role userRole;

    @BeforeEach
    void setUp() {
        adminRole = new Role();
        adminRole.setId(1L);
        adminRole.setName("ROLE_ADMIN");

        userRole = new Role();
        userRole.setId(2L);
        userRole.setName("ROLE_USER");
    }

    //saveRole
    @Test
    void saveRole_ShouldCallDaoSave_WhenRoleIsValid() {
        roleServiceImpl.saveRole(adminRole);

        verify(roleDao, times(1)).saveRole(adminRole);
    }

    //findByName
    @Test
    void findByName_ShouldReturnAdminRole_WhenNameIsAdmin() {
        when(roleDao.findByName("ROLE_ADMIN")).thenReturn(adminRole);

        Role foundRole = roleServiceImpl.findByName("ROLE_ADMIN");

        assertThat(foundRole).isNotNull();
        assertThat(foundRole.getName()).isEqualTo("ROLE_ADMIN");
        assertThat(foundRole.getId()).isEqualTo(1L);
        verify(roleDao, times(1)).findByName("ROLE_ADMIN");
    }

    @Test
    void findByName_ShouldReturnUserRole_WhenNameIsUser() {
        when(roleDao.findByName("ROLE_USER")).thenReturn(userRole);

        Role foundRole = roleServiceImpl.findByName("ROLE_USER");

        assertThat(foundRole).isNotNull();
        assertThat(foundRole.getName()).isEqualTo("ROLE_USER");
        assertThat(foundRole.getId()).isEqualTo(2L);
        verify(roleDao, times(1)).findByName("ROLE_USER");
    }

    @Test
    void findByName_ShouldReturnNull_WhenRoleDoesNotExist() {
        when(roleDao.findByName("ROLE_NOT_EXIST")).thenReturn(null);

        Role foundRole = roleServiceImpl.findByName("ROLE_NOT_EXIST");

        assertThat(foundRole).isNull();
        verify(roleDao, times(1)).findByName("ROLE_NOT_EXIST");
    }
}
