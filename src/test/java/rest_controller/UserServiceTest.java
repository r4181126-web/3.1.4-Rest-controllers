package rest_controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;
import rest_controller.dao.UserDao;
import rest_controller.model.User;
import rest_controller.service.UserServiceImpl;

import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {
    @Mock
    private UserDao userDao;
    @Mock
    private PasswordEncoder passwordEncoder;
    @InjectMocks
    private UserServiceImpl userServiceImpl;

    private User testUser;
    private User anotherUser;

    @BeforeEach
    void setUp() {
        testUser = new User();
        testUser.setId(1L);
        testUser.setUsername("andrey");
        testUser.setName("Андрей");
        testUser.setSurName("Рясной");
        testUser.setDepartment("СНГ");
        testUser.setSalary(150000);
        testUser.setPassword("101");

        anotherUser = new User();
        anotherUser.setId(2L);
        anotherUser.setUsername("vika");
        anotherUser.setName("Вика");
        anotherUser.setSurName("Рясная");
        anotherUser.setDepartment("Dree");
        anotherUser.setSalary(0);
        anotherUser.setPassword("1234");
    }

    //saveUser
    @Test
    void saveUser_ShouldEncodePassword_WhenUserIsSaved() {
        String rawPassword = testUser.getPassword();
        String encodedPassword = "encodedPassword123";
        when(passwordEncoder.encode(rawPassword)).thenReturn(encodedPassword);

        userServiceImpl.saveUser(testUser);

        assertThat(testUser.getPassword()).isEqualTo(encodedPassword);
        verify(userDao, times(1)).saveUser(testUser);
        verify(passwordEncoder, times(1)).encode(rawPassword);
    }

    @Test
    void saveUser_ShouldCallDaoSave_WhenUserIsValid() {
        when(passwordEncoder.encode(anyString())).thenReturn("encodedPassword");

        userServiceImpl.saveUser(testUser);

        verify(userDao, times(1)).saveUser(testUser);
    }

    //findByUsername
    @Test
    void findByUsername_ShouldReturnUser_WhenUserExists() {
        when(userDao.findByUsername("andrey")).thenReturn(testUser);

        User foundUser = userServiceImpl.findByUsername("andrey");

        assertThat(foundUser).isNotNull();
        assertThat(foundUser.getUsername()).isEqualTo("andrey");
        assertThat(foundUser.getName()).isEqualTo("Андрей");
        verify(userDao, times(1)).findByUsername("andrey");
    }

    @Test
    void findByUsername_ShouldReturnNull_WhenUserDoesNotExist() {
        when(userDao.findByUsername("unknown")).thenReturn(null);

        User foundUser = userServiceImpl.findByUsername("unknown");

        assertThat(foundUser).isNull();
        verify(userDao, times(1)).findByUsername("unknown");
    }

    //getAllUsers
    @Test
    void getAllUsers_ShouldReturnListOfUsers_WhenUsersExist() {
        List<User> expectedUsers = Arrays.asList(testUser, anotherUser);
        when(userDao.getAllUsers()).thenReturn(expectedUsers);

        List<User> actualUsers = userServiceImpl.getAllUsers();

        assertThat(actualUsers).isNotNull();
        assertThat(actualUsers).hasSize(2);
        assertThat(actualUsers).containsExactly(testUser, anotherUser);
        verify(userDao, times(1)).getAllUsers();
    }

    @Test
    void getAllUsers_ShouldReturnEmptyList_WhenNoUsersExist() {
        when(userDao.getAllUsers()).thenReturn(Arrays.asList());

        List<User> actualUsers = userServiceImpl.getAllUsers();

        assertThat(actualUsers).isEmpty();
        verify(userDao, times(1)).getAllUsers();
    }

    //getUserById
    @Test
    void getUserById_ShouldReturnUser_WhenIdExists() {
        when(userDao.getUserById(1L)).thenReturn(testUser);

        User foundUser = userServiceImpl.getUserById(1L);

        assertThat(foundUser).isNotNull();
        assertThat(foundUser.getId()).isEqualTo(1L);
        assertThat(foundUser.getUsername()).isEqualTo("andrey");
        verify(userDao, times(1)).getUserById(1L);
    }

    @Test
    void getUserById_ShouldReturnNull_WhenIdDoesNotExist() {
        when(userDao.getUserById(999L)).thenReturn(null);

        User foundUser = userServiceImpl.getUserById(999L);

        assertThat(foundUser).isNull();
        verify(userDao, times(1)).getUserById(999L);
    }

    //deleteUser
    @Test
    void deleteUser_ShouldCallDaoRemove_WhenIdIsValid() {
        userServiceImpl.deleteUser(1L);

        verify(userDao, times(1)).removeUserById(1L);
    }

    @Test
    void deleteUser_ShouldNotThrowException_WhenIdDoesNotExist() {
        userServiceImpl.deleteUser(999L);

        verify(userDao, times(1)).removeUserById(999L);
    }

    //updateUser
    @Test
    void updateUser_ShouldEncodePassword_WhenPasswordIsNew() {
        String rawPassword = "newRawPassword";
        String encodedPassword = "newEncodedPassword123";
        testUser.setPassword(rawPassword);
        when(passwordEncoder.encode(rawPassword)).thenReturn(encodedPassword);

        userServiceImpl.updateUser(testUser);

        assertThat(testUser.getPassword()).isEqualTo(encodedPassword);
        verify(userDao, times(1)).updateUser(testUser);
        verify(passwordEncoder, times(1)).encode(rawPassword);
    }

    @Test
    void updateUser_ShouldNotEncodePassword_WhenPasswordAlreadyEncoded() {
        String alreadyEncodedPassword = "$2a$10$encodedPasswordThatStartsWithDollar2a";
        testUser.setPassword(alreadyEncodedPassword);

        userServiceImpl.updateUser(testUser);

        assertThat(testUser.getPassword()).isEqualTo(alreadyEncodedPassword);
        verify(passwordEncoder, never()).encode(anyString());
        verify(userDao, times(1)).updateUser(testUser);
    }

    @Test
    void updateUser_ShouldCallDaoUpdate_WhenUserIsValid() {
        when(passwordEncoder.encode(anyString())).thenReturn("encodedPassword");

        userServiceImpl.updateUser(testUser);

        verify(userDao, times(1)).updateUser(testUser);
    }
}
