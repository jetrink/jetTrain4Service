package edu.utdallas.utdesign.teach4service.db;

import edu.utdallas.utdesign.teach4service.db.entities.Subject;
import edu.utdallas.utdesign.teach4service.db.entities.Tutor;
import edu.utdallas.utdesign.teach4service.db.entities.TutorExpertise;
import io.dropwizard.hibernate.AbstractDAO;
import org.hibernate.SessionFactory;

import java.io.Serializable;
import java.util.Optional;

public class TutorExpertiseDAO extends AbstractDAO<TutorExpertise>
{
    public TutorExpertiseDAO(SessionFactory factory){
        super(factory);
    }

    @Override
    public TutorExpertise get(Serializable id){
        return super.get(id);
    }

    public Optional<TutorExpertise> exists(Tutor tutor, Subject subject)
    {
        return currentSession().createNativeQuery("SELECT * FROM `TutorExpertise` WHERE `tutorId` = :tutorId and `subjectId` =:subId", TutorExpertise.class)
                .setParameter("tutorId",tutor.getId()).setParameter("subId",subject.getId())
                .uniqueResultOptional();
    }

    public TutorExpertise create(Tutor tutor, Subject subject){
        TutorExpertise tutorExpertise = new TutorExpertise();
        tutorExpertise.setTutorId(tutor.getId());
        tutorExpertise.setTutor(tutor);
        tutorExpertise.setSubjectId(subject.getId());
        tutorExpertise.setSubject(subject);

        return persist(tutorExpertise);
    }
}
