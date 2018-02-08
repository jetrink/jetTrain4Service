package edu.utdallas.utdesign.teach4service.db;

import edu.utdallas.utdesign.teach4service.db.entities.Days;
import edu.utdallas.utdesign.teach4service.db.entities.Schedule;
import edu.utdallas.utdesign.teach4service.db.entities.Sessions;
import edu.utdallas.utdesign.teach4service.db.entities.Tutor;
import io.dropwizard.hibernate.AbstractDAO;
import org.hibernate.SessionFactory;

import java.io.Serializable;
import java.util.List;

public class ScheduleDAO extends AbstractDAO<Schedule>
{
    public ScheduleDAO(SessionFactory factory){
        super(factory);
    }

    @Override
    public Schedule get(Serializable id)
    {
        return super.get(id);
    }

    public List<Schedule> getFromTutorID(long id)
    {
        return currentSession().createNativeQuery("SELECT * FROM `Schedules` WHERE `tutorID` = :tId ", Schedule.class)
                .setParameter("tId", id)
                .getResultList();

    }

    public Schedule create(Tutor t, String day, String time){
        Schedule s = new Schedule();
        s.setTutorId(t.getId());
        s.setTutor(t);
        s.setTimeAvailableDescription(time);
        Days d = Days.valueOf(day);
        s.setDayAvailable(d);
        return persist(s);
    }

}
