package edu.utdallas.utdesign.teach4service.db;

import edu.utdallas.utdesign.teach4service.db.entities.Person;
import edu.utdallas.utdesign.teach4service.db.entities.Student;
import edu.utdallas.utdesign.teach4service.db.entities.Tutor;
import edu.utdallas.utdesign.teach4service.db.entities.User;
import io.dropwizard.hibernate.AbstractDAO;
import org.hibernate.SessionFactory;
import org.mindrot.jbcrypt.BCrypt;

import java.io.Serializable;
import java.util.List;
import java.util.Optional;

public class UserDAO extends AbstractDAO<User>
{
    public UserDAO(SessionFactory sessionFactory)
    {
        super(sessionFactory);
    }

    @Override
    public User get(Serializable id)
    {
        return super.get(id);
    }


    public Optional<User> getByUsername(String username)
    {
        return currentSession().createNativeQuery("SELECT * FROM `Users` WHERE `username` = :name", User.class)
                .setParameter("name", username)
                .uniqueResultOptional();
    }

    public User getByExactUsername(String username)
    {
        return currentSession().createNativeQuery("SELECT * FROM `Users` WHERE `username` = :name", User.class)
                .setParameter("name", username)
                .getSingleResult();
    }
    public Optional<User> getByEmail(String email)
    {
        return currentSession().createNativeQuery("SELECT * FROM `Users` WHERE `email` = :email", User.class)
                .setParameter("email", email)
                .uniqueResultOptional();
    }

    public Optional<User> getByTutorId(long tid)
    {
        return currentSession().createNativeQuery("SELECT * FROM `Users` WHERE `tutorId` = :id", User.class)
                .setParameter("id", tid)
                .uniqueResultOptional();
    }

    public Optional<User> getByStudentId(long sid)
    {
        return currentSession().createNativeQuery("SELECT * FROM `Users` WHERE `studentId` = :id", User.class)
                .setParameter("id", sid)
                .uniqueResultOptional();
    }

    public List<Student> getManagedStudents(long id){
        return currentSession().createNativeQuery("SELECT * FROM `Students` WHERE `managerId` = :mId", Student.class)
                .setParameter("mId",id)
                .getResultList();
    }

    public User create(String username, String email, String password, String ip)
    {
        User user = new User();

        user.setUsername(username);
        user.setEmail(email);
        user.setPassword(BCrypt.hashpw(password, BCrypt.gensalt()));
        user.setLastLoginIp(ip);

        return persist(user);
    }
}
