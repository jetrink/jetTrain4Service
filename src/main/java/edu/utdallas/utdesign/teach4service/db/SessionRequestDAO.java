package edu.utdallas.utdesign.teach4service.db;

import edu.utdallas.utdesign.teach4service.db.entities.SessionRequest;
import edu.utdallas.utdesign.teach4service.db.entities.SessionRequestStatus;
import io.dropwizard.hibernate.AbstractDAO;
import org.hibernate.SessionFactory;
import org.joda.time.DateTime;

import java.io.Serializable;
import java.time.ZonedDateTime;
import java.util.List;

public class SessionRequestDAO extends AbstractDAO<SessionRequest>
{
    public SessionRequestDAO(SessionFactory factory){
        super(factory);
    }

    @Override
    public SessionRequest get(Serializable id)
    {
        return super.get(id);
    }

    public List<SessionRequest> getStudentRequests(long sId){
        return currentSession().createNativeQuery("SELECT * FROM `SessionRequests` WHERE `requestedFor` = :sId", SessionRequest.class)
                .setParameter("sId", sId)
                .getResultList();
    }

    public List<SessionRequest> getTutorRequests(long tId){
        return currentSession().createNativeQuery("SELECT * FROM `SessionRequests` WHERE `requestedTo` = :tId", SessionRequest.class)
                .setParameter("tId", tId)
                .getResultList();
    }

    public SessionRequest create(long studentId, long tutorId, long subjectId, ZonedDateTime sessionDate){
        SessionRequest sr =  new SessionRequest();
        sr.setRequestedFor(studentId);
        sr.setRequestedTo(tutorId);
        sr.setStatus(SessionRequestStatus.PENDING);
        sr.setSubjectId(subjectId);
        sr.setSessionDateTime(sessionDate);

        return persist(sr);
    }
}
