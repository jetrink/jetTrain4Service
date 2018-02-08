package edu.utdallas.utdesign.teach4service.db;

import edu.utdallas.utdesign.teach4service.db.entities.StudentReview;
import edu.utdallas.utdesign.teach4service.db.entities.TutorReview;
import io.dropwizard.hibernate.AbstractDAO;
import org.hibernate.SessionFactory;

import java.io.Serializable;
import java.util.List;

public class TutorReviewsDAO extends AbstractDAO<TutorReview>
{

    public TutorReviewsDAO(SessionFactory factory){
        super(factory);
    }

    @Override
    public TutorReview get(Serializable id)
    {
        return super.get(id);
    }

    public List <TutorReview> getTutorReviews(long id)
    {
        return currentSession().createNativeQuery("SELECT * FROM `TutorReview` WHERE `tutorId` = :sId", TutorReview.class)
                .setParameter("sId", id)
                .getResultList();

    }

    public TutorReview create(long studentId, long tutorId, long knowlage, long helpful, long friendlyness,long prepared)
    {
        TutorReview review = new TutorReview();
        review.setStudentId(studentId);
        review.setTutorId(tutorId);
        review.setKnowledge(knowlage);
        review.setHelpful(helpful);
        review.setFriendlyness(friendlyness);
        review.setPrepared(prepared);

        return  persist(review);
    }
}
