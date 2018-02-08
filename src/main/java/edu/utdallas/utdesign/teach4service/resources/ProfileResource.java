package edu.utdallas.utdesign.teach4service.resources;

import com.google.common.collect.Maps;
import edu.utdallas.utdesign.teach4service.auth.AuthHelper;
import edu.utdallas.utdesign.teach4service.auth.SessionToken;
import edu.utdallas.utdesign.teach4service.auth.csrfAuth.NewAuthCreds;
import edu.utdallas.utdesign.teach4service.db.*;
import edu.utdallas.utdesign.teach4service.db.entities.*;
import edu.utdallas.utdesign.teach4service.resources.requests.*;
import io.dropwizard.auth.Auth;
import io.dropwizard.hibernate.UnitOfWork;

import javax.validation.Valid;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

@Path("/profiles")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class ProfileResource
{
    private final UserDAO           udao;
    private final StudentDAO        sdao;
    private final PersonDAO         pdao;
    private final SubjectDAO        subdao;
    private final TutorDAO          tdao;
    private final TutorExpertiseDAO tedao;
    private final ScheduleDAO       scdao;
    private final AuthHelper        authHelper;

    public ProfileResource(UserDAO udao, StudentDAO sdao, PersonDAO pdao, SubjectDAO subdao, TutorDAO tdao, TutorExpertiseDAO tedao, ScheduleDAO scdao, AuthHelper authHelper)
    {
        this.udao = udao;
        this.sdao = sdao;
        this.pdao = pdao;
        this.subdao = subdao;
        this.tdao = tdao;
        this.tedao = tedao;
        this.scdao = scdao;
        this.authHelper = authHelper;
    }

    @GET
    @UnitOfWork
    @Path("/user")
    public Person getProfile(@Auth SessionToken auth)
    {
        User user = udao.get(auth.getUserId());
        if(auth.getTutorId() != 0)
        {
            return user.getTutor().getPerson();
        }
        if(auth.getStudentId() != 0)
        {
            return user.getStudent().getPerson();
        }
        if(auth.isStudentManager())
        {
            return pdao.get(user.getManagerPersonId());
        }
        throw new ForbiddenException("You have no profile setup");
    }

    @PUT
    @UnitOfWork
    @Path("/student")
    public Student profileStudentUpdate(@Auth SessionToken auth, @Valid ProfileUpdateRequestStudent update)
    {
        if(auth.getStudentId() != 0)
        {

            Student student = sdao.get(auth.getStudentId());
            Person person = student.getPerson();

            if(update.getGradeLevel() != 0)
            {
                student.setGradeLevel(update.getGradeLevel());
            }

            updatePerson(update, person);

            return student;
        }
        throw new ForbiddenException("User is not a student!");
    }

    @GET
    @UnitOfWork
    @Path("/student")
    public Student profileStudentRetrieve(@Auth SessionToken auth)
    {
        if(auth.getStudentId() != 0)
        {
            return sdao.get(auth.getStudentId());
        }
        throw new ForbiddenException("User is not a student!");
    }

    @GET
    @UnitOfWork
    @Path("/student/{id}")
    public Student profileStudentRetrieve(@Auth SessionToken auth,@PathParam("id") @Min(1) long id)
    {   Student student = sdao.get(id);

        if(student.getPerson() != null)
        {
            return student;
        }
        throw new ForbiddenException("User is not a student!");
    }



    @PUT
    @UnitOfWork
    @Path("/tutor")
    public Tutor profileTutorUpdate(@Auth SessionToken auth, @Valid ProfileUpdateRequestTutor update)
    {
        if(auth.getTutorId() != 0)
        {

            Tutor tutor = tdao.get(auth.getTutorId());
            Person person = tutor.getPerson();

            if(update.getExpYears() != 0)
            {
                tutor.setExpYears(update.getExpYears());
            }

            if(update.getExpertise() != null)                 // Expertise is subject list
            {
                tutor.setExpertise(update.getExpertise());
            }

            if(update.getSchedule() != null)
            {
                List<Schedule> schedule = scdao.getFromTutorID(tutor.getId());

                schedule = update.getSchedule();
            }

            updatePerson(update, person);

            return tutor;
        }
        throw new ForbiddenException("User is not a tutor!");
    }

    @GET
    @UnitOfWork
    @Path("/tutor")
    public Tutor profileTutorRetrieve(@Auth SessionToken auth)
    {
        if(auth.getTutorId() != 0)
        {
            return tdao.get(auth.getTutorId());
        }
        throw new ForbiddenException("User is not a tutor!");
    }

    @GET
    @UnitOfWork
    @Path("/tutor/{userName}")
    public Tutor profileTutorRetrieve(@Auth SessionToken auth, @PathParam("userName") String userName)
    {   User user = udao.getByExactUsername(userName);

        if(user.getTutorId() != 0)
        {
            return tdao.get(user.getTutorId());
        }
        throw new ForbiddenException("User is not a tutor!");
    }

    @GET
    @UnitOfWork
    @Path("/schedule/{userName}")
    public List<Schedule> profileScheduleRetrieve(@Auth SessionToken auth, @PathParam("userName") String userName)
    {   User user = udao.getByExactUsername(userName);

        if(user.getTutorId() != 0)
        {
            Tutor tutor = tdao.get(user.getTutorId());

            return scdao.getFromTutorID(tutor.getId());
        }
        throw new ForbiddenException("User is not a tutor!");
    }

    @PUT
    @UnitOfWork
    @Path("/parent")
    public Person profileParentUpdate(@Auth SessionToken auth, @Valid ProfileUpdateRequest update)
    {
        if(auth.isStudentManager())
        {
            User user = udao.get(auth.getUserId());

            Person person = pdao.get(user.getManagerPersonId());
            updatePerson(update, person);
            return person;
        }
        throw new ForbiddenException("User is not a parent!");
    }

    private void updatePerson(ProfileUpdateRequest update, Person original)
    {
        if(update.getFirstName() != null)
        {
            original.setFirstName(update.getFirstName());
        }

        if(update.getLastName() != null)
        {
            original.setLastName(update.getLastName());
        }

        if(update.getDateOfBirth() != null)
        {
            original.setDateOfBirth(update.getDateOfBirth());
        }

        if(update.getStreetAddress() != null)
        {
            original.setStreetAddress(update.getStreetAddress());
        }

        if(update.getCity() != null)
        {
            original.setCity(update.getCity());
        }

        if(update.getState() != null)
        {
            original.setState(update.getState());
        }

        if(update.getCountry() != null)
        {
            original.setCountry(update.getCountry());
        }

        if(update.getPostalCode() != null)
        {
            original.setPostalCode(update.getPostalCode());
        }
    }

    @GET
    @UnitOfWork
    @Path("/parent")
    public Person profileParentRetrieve(@Auth SessionToken auth)
    {
        if(auth.isStudentManager())
        {
            User user = udao.get(auth.getUserId());
            return pdao.get(user.getManagerPersonId());
        }
        throw new ForbiddenException("User is not a parent!");
    }
    @GET
    @UnitOfWork
    @Path("/parent/{id}")
    public Person profileParentRetrieve(@Auth SessionToken auth, @PathParam("id") @Min(1) long id)
    {   User user = udao.get(id);

        if(user.isStudentManager())
        {
            return pdao.get(user.getManagerPersonId());
        }
        throw new ForbiddenException("User is not a parent!");
    }

    @POST
    @UnitOfWork
    @Path("/studentsetup")
    public Response studentProfileSetup(@Auth SessionToken auth, @Valid ProfileSetupRequest setup)
    {
        if(auth.getStudentId() == 0)
        {
            User user = udao.get(auth.getUserId());
            Date dob = setup.getDateOfBirth();
            Person person = pdao.create(setup.getFirstName(), setup.getLastName(), dob, setup.getStreetAddress(), setup.getCity(), setup.getState(), setup.getCountry(), setup.getPostalCode());
            Student student = sdao.create(person);
            student.setGradeLevel(setup.getGradeLevel());
            user.setStudent(student);
            user.setStudentId(student.getId());
            NewAuthCreds creds = authHelper.buildCredentials(user);

            return Response.ok(user).cookie(creds.getCookie())
                    .header(AuthHelper.CSRF_HEADER, creds.getHeader()).build();
        }
        throw new ForbiddenException("Student account is already setup");
    }

    @POST
    @UnitOfWork
    @Path("/tutorsetup")
    public Response tutorProfileSetup(@Auth SessionToken auth, @Valid TutorProfileSetupRequest setup)
    {
        if(auth.getTutorId() == 0)
        {
            User user = udao.get(auth.getUserId());
            Date dob = setup.getDateOfBirth();
            Person person = pdao.create(setup.getFirstName(), setup.getLastName(), dob, setup.getStreetAddress(), setup.getCity(), setup.getState(), setup.getCountry(), setup.getPostalCode());
            ArrayList<Long> userExpertise = setup.getExpertise();
            Tutor tutor = tdao.create(person, setup.getExpYears());
            for(Long anUserExpertise : userExpertise)
            {
                Subject subject = subdao.get(anUserExpertise);
                if(!tedao.exists(tutor, subject).isPresent())
                {
                    tedao.create(tutor, subject);
                }
            }
            ArrayList<ScheduleSetupRequest> s = setup.getSchedule();
            for(ScheduleSetupRequest value : s)
            {
                scdao.create(tutor, value.getDay(), value.getTime());
            }
            user.setTutorId(tutor.getId());

            NewAuthCreds creds = authHelper.buildCredentials(user);

            return Response.ok(user)
                    .cookie(creds.getCookie())
                    .header(AuthHelper.CSRF_HEADER, creds.getHeader())
                    .build();
        }
        throw new ForbiddenException("Tutor account is already setup");
    }

    @POST
    @UnitOfWork
    @Path("/managersetup")
    public Response managerProfileSetup(@Auth SessionToken auth, @Valid ProfileSetupRequest setup)
    {
        if(!auth.isStudentManager())
        {
            User user = udao.get(auth.getUserId());
            Date dob = setup.getDateOfBirth();
            Person person = pdao.create(setup.getFirstName(), setup.getLastName(), dob, setup.getStreetAddress(), setup.getCity(), setup.getState(), setup.getCountry(), setup.getPostalCode());
            user.setManagerPersonId(person.getId());
            user.setStudentManager(true);
            NewAuthCreds creds = authHelper.buildCredentials(user);

            return Response.ok(user).cookie(creds.getCookie())
                    .header(AuthHelper.CSRF_HEADER, creds.getHeader()).build();
        }
        throw new ForbiddenException("Manager account is already setup");
    }

    @GET
    @UnitOfWork
    @Path("/subjects/{field}")
    public Response getSubjects(@PathParam("field") @NotNull String field)
    {
        return Response.ok(subdao.getSubjects(field)).build();
    }

    @GET
    @UnitOfWork
    @Path("/subjects")
    public Response getAllSubjects()
    {
        return Response.ok(subdao.getSubjects()).build();
    }

    @GET
    @UnitOfWork
    @Path("/isapproved")
    public Response isApproved(@Auth SessionToken auth)
    {
        if(auth.getTutorId() == 0)
        {
            HashMap<String, Boolean> h = Maps.newHashMap();
            h.put("approved", true);
            return Response.ok(h).build();
        }
        return Response.ok(tdao.get(auth.getTutorId())).build();
    }
}
