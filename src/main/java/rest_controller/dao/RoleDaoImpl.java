package rest_controller.dao;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import rest_controller.model.Role;


import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.util.List;

@Repository
public class RoleDaoImpl implements RoleDao {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    @Transactional
    public void saveRole(Role role) {
        entityManager.persist(role);
    }

    @Override
    @Transactional
    public void removeRoleById(long id) {
        Role role = entityManager.find(Role.class, id);
        if (role != null) {
            entityManager.remove(role);
        }
    }

    @Override
    public List<Role> getAllRole() {
        return entityManager.createQuery("SELECT u FROM Role u ORDER BY u.id", Role.class).getResultList();
    }

    @Override
    @Transactional
    public void cleanRoleTable() {
        Query query = entityManager.createQuery("DELETE FROM Role");
        query.executeUpdate();
    }

    @Override
    @Transactional
    public void updateRole(Role role) {
        entityManager.merge(role);
    }

    @Override
    public Role getRoleById(long id) {
        return entityManager.find(Role.class, id);
    }

    @Override
    @Transactional
    public Role findByName(String name) {
        try {
            return entityManager.createQuery("SELECT r FROM Role r " +
                            "WHERE r.name = :name", Role.class).setParameter("name", name).
                    getSingleResult();
        } catch (javax.persistence.NonUniqueResultException e) {
            return null;
        }
    }
}
