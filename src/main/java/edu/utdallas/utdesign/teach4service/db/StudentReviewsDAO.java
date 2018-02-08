package edu.utdallas.utdesign.teach4service.db;

import edu.utdallas.utdesign.teach4service.db.entities.Sessions;
import edu.utdallas.utdesign.teach4service.db.entities.StudentReview;
import io.dropwizard.hibernate.AbstractDAO;
import org.hibernate.SessionFactory;

import java.io.Serializable;
import java.time.ZonedDateTime;
import java.util.List;

public class StudentReviewsDAO extends AbstractDAO<StudentReview>
{

    public StudentReviewsDAO(SessionFactory factory){
        super(factory);
    }

    @Override
    public StudentReview get(Serializable id)
    {
        return super.get(id);
    }

    public List <StudentReview> getStudentReviews(long id)
    {
        return currentSession().createNativeQuery("SELECT * FROM `StudentReview` WHERE `studentId` = :sId", StudentReview.class)
                .setParameter("sId", id)
                .getResultList();

    }

    public StudentReview create(long studentId, long tutorId, long comp, long effort, long participation,long respect, long preperation)
    {
        StudentReview review = new StudentReview();
        review.setStudentId(studentId);
        review.setTutorId(tutorId);
        review.setComprehension(comp);
        review.setEffort(effort);
        review.setParticipation(participation);
        review.setRespectfullness(respect);
        review.setPreperation(preperation);

        return  persist(review);
    }
}
