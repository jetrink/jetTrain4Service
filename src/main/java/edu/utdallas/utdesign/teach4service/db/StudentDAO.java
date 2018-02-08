package edu.utdallas.utdesign.teach4service.db;

import edu.utdallas.utdesign.teach4service.db.entities.Student;
import edu.utdallas.utdesign.teach4service.db.entities.Person;
import edu.utdallas.utdesign.teach4service.db.entities.User;
import io.dropwizard.hibernate.AbstractDAO;
import org.hibernate.SessionFactory;

import java.io.Serializable;
import java.util.Optional;

public class StudentDAO extends AbstractDAO<Student>
{
    public StudentDAO(SessionFactory factory){
        super(factory);
    }

    @Override
    public Student get(Serializable id)
    {
        return super.get(id);
    }

    public Optional<User> getuser(long studentId){
        return currentSession().createNativeQuery("SELECT * FROM `Users` WHERE `studentId` = :studentId", User.class)
                .setParameter("studentId", studentId)
                .uniqueResultOptional();
    }

    public Student create(Person person){
        Student student = new Student();
        student.setPerson(person);

        return persist(student);
    }


}

