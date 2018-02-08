package edu.utdallas.utdesign.teach4service.resources;


import edu.utdallas.utdesign.teach4service.auth.SessionToken;
import edu.utdallas.utdesign.teach4service.db.*;
import edu.utdallas.utdesign.teach4service.db.entities.*;
import edu.utdallas.utdesign.teach4service.resources.requests.AvailableRequest;
import edu.utdallas.utdesign.teach4service.resources.requests.ProfileSetupRequest;
import edu.utdallas.utdesign.teach4service.resources.responses.ParentDashboardResponse;
import edu.utdallas.utdesign.teach4service.resources.responses.StudentDashboardResponse;
import edu.utdallas.utdesign.teach4service.resources.responses.TutorDashboardResponse;
import io.dropwizard.auth.Auth;
import io.dropwizard.hibernate.UnitOfWork;
import org.joda.time.DateTime;

import javax.annotation.security.RolesAllowed;
import javax.validation.Valid;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.util.Date;
import java.util.List;

import static edu.utdallas.utdesign.teach4service.auth.AuthHelper.ROLE_STUDENT;
import static edu.utdallas.utdesign.teach4service.auth.AuthHelper.ROLE_TUTOR;

@Path("/dashboard")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)

public class DashboardResource
{

    private final StudentDAO        sdao;
    private final UserDAO           udao;
    private final TutorDAO          tdao;
    private final SessionDAO        sesdao;
    private final TutorReviewsDAO   trdao;
    private final StudentReviewsDAO srdao;
    private final PersonDAO         pdao;


    public DashboardResource(UserDAO udao, StudentDAO sdao, SessionDAO sesdao, TutorReviewsDAO trdao, TutorDAO tdao, StudentReviewsDAO srdao, PersonDAO pdao)
    {
        this.sdao = sdao;
        this.tdao = tdao;
        this.sesdao = sesdao;
        this.trdao = trdao;
        this.srdao = srdao;
        this.udao = udao;
        this.pdao = pdao;
    }

    @POST
    @UnitOfWork
    @Path("/isavailable")
    public Tutor setAvailability(@Auth SessionToken auth, @Valid AvailableRequest setup)
    {
        if(auth.getTutorId() != 0)
        {
            Tutor t = tdao.get(auth.getTutorId());
            t.setAvailability(setup.isAvailable());
            return t;
        }
        throw new ForbiddenException("Unable to set availability");
    }

    @POST
    @UnitOfWork
    @Path("/addstudent")
    public Student addManagerStudent(@Auth SessionToken auth, @Valid ProfileSetupRequest setup)
    {
        if(auth.isStudentManager())
        {
            Date dob = setup.getDateOfBirth();
            Person person = pdao.create(setup.getFirstName(), setup.getLastName(), dob, setup.getStreetAddress(), setup.getCity(), setup.getState(), setup.getCountry(), setup.getPostalCode());
            Student student = sdao.create(person);
            student.setManagerId(auth.getUserId());
            return student;
        }
        throw new ForbiddenException("You are not authorized");
    }

    @GET
    @UnitOfWork
    @Path("/student")
    @RolesAllowed(ROLE_STUDENT)
    public StudentDashboardResponse DashboardStudentRetrieve(@Auth SessionToken auth)
    {
        final Date checkUpcomingTime = DateTime.now().minusHours(4).toDate();
        final Date checkPastTime = DateTime.now().toDate();

        StudentDashboardResponse out = new StudentDashboardResponse();

        out.setSessions(sesdao.getStudentUpcomingSessions(auth.getStudentId(), checkUpcomingTime));
        out.setRecentTutors(sesdao.getRecentTutors(auth.getStudentId(), checkPastTime));
        out.setStudentReviews(srdao.getStudentReviews(auth.getStudentId()));

        return out;
    }

    @GET
    @UnitOfWork
    @Path("/Tutor")
    @RolesAllowed(ROLE_TUTOR)
    public TutorDashboardResponse dashboardTutorRetrieve(@Auth SessionToken auth)
    {
        final Date checkUpcomingTime = DateTime.now().minusHours(4).toDate();
        final Date checkPastTime = DateTime.now().toDate();

        TutorDashboardResponse out = new TutorDashboardResponse();

        out.setSessions(sesdao.getTutorUpcomingSessions(auth.getTutorId(), checkUpcomingTime));
        out.setRecentStudents(sesdao.getRecentStudents(auth.getTutorId(), checkPastTime));
        out.setTutorReviews(trdao.getTutorReviews(auth.getTutorId()));
        out.setOnline(tdao.get(auth.getTutorId()).isAvailability());

        return out;
    }

    @PUT
    @UnitOfWork
    @Path("/Tutor")
    public Tutor dashboardTutorSetAvailable(@Auth SessionToken auth, TutorDashboardResponse update)
    {
        if(auth.getUserId() != 0)
        {
            Tutor tutor = tdao.get(auth.getTutorId());

            tutor.setAvailability(update.isOnline());

            return tutor;
        }
        throw new NotFoundException("Tutor account not found");
    }

    @GET
    @UnitOfWork
    @Path("/Parent")
    public ParentDashboardResponse dashboardParentRetrieve(@Auth SessionToken auth)
    {
        if(auth.getUserId() != 0)
        {
            final Date checkUpcomingTime = DateTime.now().minusHours(4).toDate();
            final Date checkPastTime = DateTime.now().toDate();

            ParentDashboardResponse out = new ParentDashboardResponse();

            out.setManagedStudents(udao.getManagedStudents(auth.getUserId()));

            List<Sessions> sessions = out.getSessions();
            List<List<Tutor>> tutors = out.getRecentTutors();
            List<StudentReview> reviews = out.getStudentReviews();

            for(Student student : out.getManagedStudents())
            {
                sessions.addAll(sesdao.getStudentUpcomingSessions(student.getId(), checkUpcomingTime));
                tutors.add(sesdao.getRecentTutors(student.getId(), checkPastTime));
                reviews.addAll(srdao.getStudentReviews(student.getId()));
            }

            return out;
        }
        throw new NotFoundException("Parent account not found");
    }
}
