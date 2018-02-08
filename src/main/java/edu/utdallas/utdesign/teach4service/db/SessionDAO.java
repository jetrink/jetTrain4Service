package edu.utdallas.utdesign.teach4service.db;

import edu.utdallas.utdesign.teach4service.db.entities.SessionStatus;
import edu.utdallas.utdesign.teach4service.db.entities.Sessions;
import edu.utdallas.utdesign.teach4service.db.entities.Student;
import edu.utdallas.utdesign.teach4service.db.entities.Tutor;
import io.dropwizard.hibernate.AbstractDAO;
import org.hibernate.SessionFactory;

import java.io.Serializable;
import java.time.ZonedDateTime;
import java.util.Date;
import java.util.List;

public class SessionDAO extends AbstractDAO<Sessions>
{

    public SessionDAO(SessionFactory factory)
    {
        super(factory);
    }

    @Override
    public Sessions get(Serializable id)
    {
        return super.get(id);
    }

    public List<Sessions> getStudentSessions(long id)
    {
        return currentSession().createNativeQuery("SELECT * FROM `Sessions` WHERE `studentId` = :sId AND `status`=:s", Sessions.class)
                .setParameter("sId", id)
                .setParameter("s", SessionStatus.ENDED.name())
                .getResultList();
    }

    public List<Sessions> getStudentUpcomingSessions(long studentId, Date date)
    {
        String sql = "SELECT * " +
                "FROM `Sessions` " +
                "WHERE `studentId` = :id AND `startDateTime` > :sTime " +
                "ORDER BY `startDateTime` ASC " +
                "LIMIT 10";
        return currentSession().createNativeQuery(sql, Sessions.class)
                .setParameter("id", studentId)
                .setParameter("sTime", date)
                .getResultList();
    }

    public List<Tutor> getRecentTutors(long studentId, Date date)
    {
        String sql = "SELECT `Tutors`.* " +
                "FROM `Sessions` " +
                "INNER JOIN `Tutors` ON `Sessions`.`tutorId` = `Tutors`.`id` " +
                "WHERE `studentId` = :id AND `endDateTime` < :eTime " +
                "ORDER BY `endDateTime` ASC " +
                "LIMIT 10";
        return currentSession().createNativeQuery(sql, Tutor.class)
                .setParameter("id", studentId)
                .setParameter("eTime", date)
                .getResultList();
    }

    public List<Sessions> getTutorSessions(long id)
    {
        return currentSession().createNativeQuery("SELECT * FROM `Sessions` WHERE `tutorId` = :tId AND `status`=:s", Sessions.class)
                .setParameter("tId", id)
                .setParameter("s", SessionStatus.ENDED.name())
                .getResultList();
    }

    public List<Sessions> getTutorUpcomingSessions(long id, Date date)
    {
        String sql = "SELECT * " +
                "FROM `Sessions` " +
                "WHERE `tutorId` = :id AND `startDateTime` > :sTime " +
                "ORDER BY `startDateTime` ASC " +
                "LIMIT 10";
        return currentSession().createNativeQuery(sql, Sessions.class)
                .setParameter("id", id)
                .setParameter("sTime", date)
                .getResultList();
    }

    public List<Student> getRecentStudents(long tutorId, Date date)
    {
        String sql = "SELECT `Students`.* " +
                "FROM `Sessions` " +
                "INNER JOIN `Students` ON `Sessions`.`studentId` = `Students`.`id` " +
                "WHERE `tutorId` = :id AND `endDateTime` < :eTime " +
                "ORDER BY `endDateTime` ASC " +
                "LIMIT 10";
        return currentSession().createNativeQuery(sql, Student.class)
                .setParameter("id", tutorId)
                .setParameter("eTime", date)
                .getResultList();
    }

    public Sessions create(long studentId, long tutorId, long subjectId, ZonedDateTime startTime)
    {
        Sessions session = new Sessions();
        session.setStudentId(studentId);
        session.setTutorId(tutorId);
        session.setSubjectId(subjectId);
        session.setStartDateTime(startTime);
        return persist(session);
    }
}
