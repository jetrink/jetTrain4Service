package edu.utdallas.utdesign.teach4service.db;

import edu.utdallas.utdesign.teach4service.db.entities.Subject;
import edu.utdallas.utdesign.teach4service.db.entities.SubjectField;
import io.dropwizard.hibernate.AbstractDAO;
import org.hibernate.SessionFactory;

import java.io.Serializable;
import java.util.List;
import java.util.Optional;

public class SubjectDAO extends AbstractDAO<Subject>
{
    public SubjectDAO(SessionFactory factory){
        super(factory);
    }

    @Override
    public Subject get(Serializable id){
        return super.get(id);
    }

    public Optional<Subject> getByName(String subName)
    {
        return currentSession().createNativeQuery("SELECT * FROM `Subjects` WHERE `name` = :name", Subject.class)
                .setParameter("name", subName)
                .uniqueResultOptional();
    }

    public List<Subject> getSubjects(String field){
        return currentSession().createNativeQuery("SELECT * FROM `Subjects` WHERE `field` = :name", Subject.class)
                .setParameter("name", field.toUpperCase())
                .getResultList();
    }

    public List<Subject> getSubjects(){
        return currentSession().createNativeQuery("SELECT * FROM `Subjects`", Subject.class)
                .getResultList();
    }

    public Subject create(String name, String field, String description){
        if(!getByName(name).isPresent()){
            Subject subject = new Subject();
            subject.setName(name);
            subject.setDescription(description);
            SubjectField s = SubjectField.valueOf(field.toUpperCase());

            subject.setField(SubjectField.valueOf(field.toUpperCase()));

            return persist(subject);
        }
        else{
            return getByName(name).get();
        }
        //TODO check if subject exists
    }



    public void delete(Subject subject) {currentSession().delete(subject);}

}
