package edu.utdallas.utdesign.teach4service.db;

import edu.utdallas.utdesign.teach4service.db.entities.AdminUser;
import io.dropwizard.hibernate.AbstractDAO;
import org.hibernate.SessionFactory;
import org.mindrot.jbcrypt.BCrypt;

import java.io.Serializable;
import java.util.List;
import java.util.Optional;

public class AdminDAO extends AbstractDAO<AdminUser>
{
    public AdminDAO(SessionFactory sessionFactory)
    {
        super(sessionFactory);
    }

    @Override
    public AdminUser get(Serializable id)
    {
        return super.get(id);
    }

    public List<AdminUser> getAll()
    {
        return currentSession().createNativeQuery("SELECT * FROM `AdminUsers`", AdminUser.class).list();
    }

    public Optional<AdminUser> getByUsername(String username)
    {
        return currentSession().createNativeQuery("SELECT * FROM `AdminUsers` WHERE `username` = :name", AdminUser.class)
                .setParameter("name", username)
                .uniqueResultOptional();
    }

    public AdminUser create(String username, String email, String password)
    {
        AdminUser user = new AdminUser();

        user.setUsername(username);
        user.setEmail(email);
        user.setPassword(BCrypt.hashpw(password, BCrypt.gensalt()));
        user.setLastLoginIp("NEVER");

        return persist(user);
    }

    public void delete(AdminUser user)
    {
        currentSession().delete(user);
    }
}
