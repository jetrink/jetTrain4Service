package edu.utdallas.utdesign.teach4service.db;

import edu.utdallas.utdesign.teach4service.db.entities.Person;
import edu.utdallas.utdesign.teach4service.db.entities.TutorExpertise;
import io.dropwizard.hibernate.AbstractDAO;
import org.hibernate.SessionFactory;
import edu.utdallas.utdesign.teach4service.db.entities.Tutor;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class TutorDAO extends AbstractDAO<Tutor>
{
    public TutorDAO(SessionFactory factory){
        super(factory);
    }

  @Override
    public Tutor get(Serializable id){
        return super.get(id);
  }

    public List<TutorExpertise> findBySubject(long id){
        return currentSession().createNativeQuery("SELECT * FROM `TutorExpertise` WHERE `subjectId` = :sId", TutorExpertise.class)
                .setParameter("sId", id)
                .getResultList();
    }

    private List<Person> getByName(String firstName, String lastName){
        if(!firstName.isEmpty() && !lastName.isEmpty()){
            return currentSession().createNativeQuery("SELECT * FROM `Persons` WHERE `firstName` =:fname and `lastName` = :name", Person.class)
                    .setParameter("name", lastName)
                    .setParameter("fname",firstName)
                    .getResultList();
        }else if(!firstName.isEmpty()){
            return currentSession().createNativeQuery("SELECT * FROM `Persons` WHERE `firstName` = :name", Person.class)
                    .setParameter("name", firstName)
                    .getResultList();
        }
        return currentSession().createNativeQuery("SELECT * FROM `Persons` WHERE `lastName` = :name", Person.class)
                .setParameter("name", lastName)
                .getResultList();
    }

    public List<Tutor> getTutors(String firstName, String LastName){
        List<Person> p = getByName(firstName,LastName);
        List <Tutor> t = new ArrayList();
        for(int i=0;i<p.size();i++){
            if(getByPersonId(p.get(i).getId()).isPresent()){
                t.add(getByPersonId(p.get(i).getId()).get());
            }
        }
        return t;
    }

    public List<Tutor> getAuthorizedTutors()
    {

        return currentSession().createNativeQuery("SELECT * FROM `Tutors` WHERE `approved` = TRUE",Tutor.class)
                .getResultList();

    }

    public List<Tutor> getUnauthorizedTutors()
    {
        return currentSession().createNativeQuery("SELECT * FROM `Tutors` WHERE `approved` = FALSE",Tutor.class)
                .getResultList();

    }

    public Optional<Tutor> getByPersonId(long id){
         return currentSession().createNativeQuery("SELECT * FROM `Tutors` WHERE `personId` = :pid", Tutor.class)
                .setParameter("pid", id)
                .uniqueResultOptional();
    }

  public Tutor create(Person person, int expYears){
        Tutor tutor  = new Tutor();
        tutor.setPerson(person);
        tutor.setExpYears(expYears);

        return persist(tutor);
  }


}