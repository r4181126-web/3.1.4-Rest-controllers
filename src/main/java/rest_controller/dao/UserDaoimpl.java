package rest_controller.dao;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import rest_controller.model.Role;
import rest_controller.model.User;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.util.List;

@Repository
public class UserDaoimpl implements UserDao {
    @PersistenceContext
    private EntityManager entityManager;

    @Override
    @Transactional
    public void saveUser(User user) {
       entityManager.persist(user);
    }

    @Override
    @Transactional
    public void removeUserById(long id) {
        User user = entityManager.find(User.class, id);
        if (user != null) {
            entityManager.remove(user);
        }
    }

    @Override
    public List<User> getAllUsers() {
        return entityManager.createQuery("SELECT u FROM User u JOIN FETCH u.roles", User.class).getResultList();
    }

    @Override
    @Transactional
    public void cleanUsersTable() {
        Query query = entityManager.createQuery("DELETE FROM User");
        query.executeUpdate();
    }

    @Override
    @Transactional
    public void updateUser(User user) {
        entityManager.merge(user);
    }

    @Override
    public User getUserById(long id) {
        return entityManager.find(User.class, id);
    }

    @Override
    public User findByUsername(String username) {
        try {
            return entityManager.createQuery(
                            "SELECT u FROM User u JOIN FETCH u.roles WHERE u.username = :username",
                            User.class).setParameter("username", username).getSingleResult();
        } catch (javax.persistence.NoResultException e) {
            return null;
        }
    }
    @Override
    @Transactional
    public void addRoleToUser(long userId, long roleId) {
        User user = entityManager.find(User.class, userId);
        Role role = entityManager.find(Role.class, roleId);
        if (user != null && role != null) {
            user.getRoles().add(role);
            entityManager.merge(user);
        }
    }
}
