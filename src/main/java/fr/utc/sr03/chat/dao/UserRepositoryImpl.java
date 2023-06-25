package fr.utc.sr03.chat.dao;

import fr.utc.sr03.chat.model.User;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.transaction.Transactional;
import java.util.List;

@Repository
public class UserRepositoryImpl implements UserRepositoryCustom {

    @PersistenceContext
    EntityManager entityManager;

    @Override
    public List<User> findAdminOnly() {
        Query query = entityManager.createQuery("SELECT u FROM User u WHERE u.admin = :admin", User.class).setParameter("admin", true);

        return query.getResultList();
    }


    @Override
    @Transactional
    public void DeleteBynom(String nom) {
         Query query =entityManager.createQuery("DELETE FROM User u WHERE u.lastName = :name").setParameter("name",nom);
        int deletedCount=query.executeUpdate();
    }

    @Override
    @Transactional
    public void disableUser(int id) {
        Query query =entityManager.createQuery("UPDATE User u SET u.enabled = 'FALSE' WHERE u.id = :id").setParameter("id",id);
        int updateCount=query.executeUpdate();
    }
}