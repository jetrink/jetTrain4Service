package edu.utdallas.utdesign.teach4service.resources;


import com.google.common.collect.Lists;
import edu.utdallas.utdesign.teach4service.auth.SessionToken;
import edu.utdallas.utdesign.teach4service.db.*;
import edu.utdallas.utdesign.teach4service.db.entities.*;
import edu.utdallas.utdesign.teach4service.resources.requests.*;
import io.dropwizard.auth.Auth;
import io.dropwizard.hibernate.UnitOfWork;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.time.format.DateTimeFormatter;
import java.util.*;

@Path("/sessions")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class SessionResource
{
    private final SessionDAO        sdao;
    private final UserDAO           udao;
    private final StudentDAO        stdao;
    private final TutorDAO          tdao;
    private final SessionRequestDAO srdao;
    private DateTimeFormatter dateFormat = DateTimeFormatter.ofPattern("MM/dd/yyyy hh:mm a");

    public SessionResource(SessionDAO sdao, UserDAO udao, StudentDAO stdao, TutorDAO tdao, SessionRequestDAO srdao)
    {
        this.sdao = sdao;
        this.udao = udao;
        this.stdao = stdao;
        this.tdao = tdao;
        this.srdao = srdao;
    }

    @GET
    @UnitOfWork
    @Path("/self/studenthistory")
    public Response studentSessionHistory(@Auth SessionToken auth)
    {
        if(auth.getStudentId() != 0)
        {
            List<Sessions> sessionsList = sdao.getStudentSessions(auth.getStudentId());
            List<HashMap<String, String>> studentSessions = new ArrayList();
            for(Sessions s : sessionsList)
            {
                HashMap<String, String> sessions = new HashMap();
                sessions.put("tutorUsername", udao.getByTutorId(s.getTutorId()).get().getUsername());
                sessions.put("subject", s.getSubject().getName());
                sessions.put("startDate", s.getStartDateTime().toString());
                sessions.put("endDate", s.getEndDateTime().toString());
                sessions.put("description", s.getDescription());
                studentSessions.add(sessions);
            }
            return Response.ok(studentSessions).build();
        }
        throw new ForbiddenException("You are not authorize to access this data");
    }

    @GET
    @UnitOfWork
    @Path("/self/tutorhistory")
    public Response tutorSessionHistory(@Auth SessionToken auth)
    {
        if(auth.getTutorId() != 0)
        {
            List<Sessions> sessionsList = sdao.getTutorSessions(auth.getTutorId());
            List<HashMap<String, String>> tutorSessions = new ArrayList();
            for(Sessions s : sessionsList)
            {
                HashMap<String, String> sessions = new HashMap();
                sessions.put("studentUsername", (udao.getByStudentId(s.getStudentId()).get().getUsername()));
                sessions.put("subject", s.getSubject().getName());
                sessions.put("startDate", s.getStartDateTime().toString());
                sessions.put("endDate", s.getEndDateTime().toString());
                sessions.put("description", s.getDescription());
                tutorSessions.add(sessions);
            }

            return Response.ok(tutorSessions).build();
        }
        throw new ForbiddenException("You are not authorize to access this data");
    }

    @GET
    @UnitOfWork
    @Path("/students/{studentId}")
    public Response managerStudentHistory(@Auth SessionToken auth, @PathParam("studentId") @Min(1) long studentId)
    {
        if(auth.isStudentManager() && udao.getManagedStudents(auth.getUserId()).contains(stdao.get(studentId)))
        {
            List<Sessions> sessionsList = sdao.getTutorSessions(studentId);
            List<HashMap<String, String>> studentSessions = new ArrayList();
            for(Sessions s : sessionsList)
            {
                HashMap<String, String> sessions = new HashMap();
                sessions.put("tutorUsername", udao.getByTutorId(s.getTutorId()).get().getUsername());
                sessions.put("subject", s.getSubject().getName());
                sessions.put("startDate", s.getStartDateTime().toString());
                sessions.put("endDate", s.getEndDateTime().toString());
                sessions.put("description", s.getDescription());
                studentSessions.add(sessions);
            }
            return Response.ok(studentSessions).build();
        }
        throw new ForbiddenException("You are not authorize to access this data");
    }

    @GET
    @UnitOfWork
    @Path("/students/requests/self")
    public Response getStudentRequests(@Auth SessionToken auth)
    {
        if(auth.getStudentId() != 0)
        {
            List<SessionRequest> srs = new ArrayList();
            srs = srdao.getStudentRequests(auth.getStudentId());
            List<HashMap<String, String>> studentRequests = new ArrayList();
            for(SessionRequest s : srs)
            {
                HashMap<String, String> request = new HashMap();
                request.put("requestId", Long.toString(s.getId()));
                request.put("requestDate", s.getCreatedOn().format(dateFormat));
                request.put("tutorUsername", udao.getByTutorId(s.getRequestedTo()).get().getUsername());
                request.put("subject", s.getSubject().getName());
                request.put("time", s.getSessionDateTime().format(dateFormat));
                request.put("status", s.getStatus().name());
                studentRequests.add(request);
            }
            return Response.ok(studentRequests).build();
        }
        throw new ForbiddenException("You are not authorize to access this data");
    }

    @GET
    @UnitOfWork
    @Path("/students/requests/{studentId}")
    public Response getAStudentRequests(@Auth SessionToken auth, @PathParam("studentId") @Min(1) long studentId)
    {
        Student student = stdao.get(studentId);

        if(auth.isStudentManager() && student != null && auth.getUserId() == student.getManagerId())
        {
            List<SessionRequest> srs  = srdao.getStudentRequests(studentId);
            List<HashMap<String, String>> studentRequests = Lists.newArrayList();

            for(SessionRequest s : srs)
            {
                HashMap<String, String> request = new HashMap();
                request.put("requestId", Long.toString(s.getId()));
                request.put("requestDate", s.getCreatedOn().format(dateFormat));
                request.put("tutorUsername", udao.getByTutorId(s.getRequestedTo()).get().getUsername());
                request.put("subject", s.getSubject().getName());
                request.put("time", s.getSessionDateTime().format(dateFormat));
                request.put("status", s.getStatus().name());
                studentRequests.add(request);
            }
            return Response.ok(studentRequests).build();
        }
        throw new ForbiddenException("You are not authorize to access this data");
    }

    @GET
    @UnitOfWork
    @Path("/tutors/requests/self")
    public Response getTutorRequests(@Auth SessionToken auth)
    {
        if(auth.getTutorId() != 0)
        {
            List<SessionRequest> srs = srdao.getTutorRequests(auth.getTutorId());
            List<HashMap<String, String>> tutorRequests = new ArrayList();
            for(SessionRequest s : srs)
            {
                HashMap<String, String> request = new HashMap();
                request.put("requestId", Long.toString(s.getId()));
                request.put("requestDate", s.getCreatedOn().format(dateFormat));

                Optional<User> studentUser = udao.getByStudentId(s.getRequestedFor());
                request.put("studentUsername", studentUser.isPresent() ? studentUser.get().getUsername() : "ManagedUser"+s.getRequestedFor());

                request.put("subject", s.getSubject().getName());
                request.put("time", s.getSessionDateTime().format(dateFormat));
                request.put("status", s.getStatus().name());
                tutorRequests.add(request);
            }
            return Response.ok(tutorRequests).build();
        }
        throw new ForbiddenException("You are not authorize to access this data");
    }

    @POST
    @UnitOfWork
    @Path("/requests/respond")
    public Response responseToRequest(@Auth SessionToken auth, SessionResponseRequest response)
    {
        if(auth.getTutorId() != 0 && srdao.get(response.getRequestId()).getRequestedTo() == auth.getTutorId())
        {
            SessionRequest sr = srdao.get(response.getRequestId());
            sr.setStatus(response.getStatus());
            if(response.getStatus().equals(SessionRequestStatus.APPROVED)){
                Sessions session = sdao.create(sr.getRequestedFor(),sr.getRequestedTo(),sr.getSubjectId(),sr.getSessionDateTime());
            }
            List<SessionRequest> srs = new ArrayList();
            srs = srdao.getTutorRequests(auth.getTutorId());
            List<HashMap<String, String>> tutorRequests = new ArrayList();
            for(SessionRequest s : srs)
            {
                HashMap<String, String> request = new HashMap();
                request.put("requestId", Long.toString(s.getId()));
                request.put("requestDate", s.getCreatedOn().format(dateFormat));

                Optional<User> studentUser = udao.getByStudentId(s.getRequestedFor());
                request.put("studentUsername", studentUser.isPresent() ? studentUser.get().getUsername() : "ManagedUser"+s.getRequestedFor());

                request.put("subject", s.getSubject().getName());
                request.put("time", s.getSessionDateTime().format(dateFormat));
                request.put("status", s.getStatus().name());
                tutorRequests.add(request);
            }
            return Response.ok(tutorRequests).build();
        }
        throw new ForbiddenException("You are not authorize to respond to this request");
    }

    @POST
    @UnitOfWork
    @Path("/findtutor/bytutor")
    public Response findByTutor(@Auth SessionToken auth, FindByTutorRequest req)
    {
        if(auth.getStudentId() != 0 || auth.isStudentManager())
        {

            User user = udao.getByUsername(req.getUsername()).get();
            if(user.getTutorId() != 0)
            {
                Tutor tutor = user.getTutor();
              if(tutor.getId() != auth.getTutorId() && tutor.isApproved()){
                  List <HashMap<String, String>> t = new ArrayList();
                  HashMap<String, String> tutorInfo = new HashMap();
                  tutorInfo.put("username", user.getUsername());
                  tutorInfo.put("state", tutor.getPerson().getState());
                  tutorInfo.put("firstName",tutor.getPerson().getFirstName());
                  tutorInfo.put("lastName",tutor.getPerson().getLastName());
                  tutorInfo.put("city",tutor.getPerson().getCity());
                  tutorInfo.put("postalCode",tutor.getPerson().getPostalCode());
                  tutorInfo.put("address",tutor.getPerson().getStreetAddress());
                  tutorInfo.put("country", tutor.getPerson().getCountry());
                  tutorInfo.put("expYears", Integer.toString(tutor.getExpYears()));
                  tutorInfo.put("available", Boolean.toString(tutor.isAvailability()));
                  Iterator<Subject> iterator = tutor.getExpertise().iterator();
                  while(iterator.hasNext())
                  {
                      Subject s = iterator.next();
                      tutorInfo.put(s.getName(), s.getDescription());
                  }
                  t.add(tutorInfo);
                  return Response.ok(t).build();
              }else{
                  return Response.ok().build();
              }
            }
        }
        throw new ForbiddenException("You are not authorize to access this data");
    }

    @POST
    @UnitOfWork
    @Path("/findtutor/bytutorname")
    public Response findByTutorName(@Auth SessionToken auth, FindByTutorNameRequest req)
    {
        if(auth.getStudentId() != 0 || auth.isStudentManager())
        {
            List<Tutor> te = tdao.getTutors(req.getFirstName(),req.getLastName());
            List<HashMap<String, String>> tutors = new ArrayList();
            for(int i = 0; i < te.size(); i++)
            {
                Tutor tutor = te.get(i);
                if(te.get(i).getId() == auth.getTutorId() || !te.get(i).isApproved()){
                    ++i;
                    if(i<te.size()){
                        tutor = te.get(i);
                    }
                    else {
                        break;
                    }
                }
                HashMap<String, String> tutorInfo = new HashMap();
                tutorInfo.put("username", udao.getByTutorId(te.get(i).getId()).get().getUsername());
                tutorInfo.put("firstName",te.get(i).getPerson().getFirstName());
                tutorInfo.put("lastName",te.get(i).getPerson().getLastName());
                tutorInfo.put("city",te.get(i).getPerson().getCity());
                tutorInfo.put("postalCode",te.get(i).getPerson().getPostalCode());
                tutorInfo.put("address",te.get(i).getPerson().getStreetAddress());
                tutorInfo.put("state", te.get(i).getPerson().getState());
                tutorInfo.put("country", te.get(i).getPerson().getCountry());
                tutorInfo.put("expYears", Integer.toString(te.get(i).getExpYears()));
                tutorInfo.put("available", Boolean.toString(te.get(i).isAvailability()));
                for(Subject s : te.get(i).getExpertise())
                {
                    tutorInfo.put(s.getName(), s.getDescription());
                }
                tutors.add(tutorInfo);
            }
            return Response.ok(tutors).build();
        }
        throw new ForbiddenException("You are not authorize to access this data");
    }

    @POST
    @UnitOfWork
    @Path("/findtutor/bysubject")
    public Response findBySubject(@Auth SessionToken auth, FindBySubject req)
    {
        if(auth.getStudentId() != 0 || auth.isStudentManager())
        {
            List<TutorExpertise> te = tdao.findBySubject(req.getSubjectId());
            List<HashMap<String, String>> tutors = new ArrayList();
            for(int i = 0; i < te.size(); i++)
            {
                Tutor tutor = te.get(i).getTutor();
                if(tutor.getId() == auth.getTutorId()|| !tutor.isApproved()){
                    ++i;
                    if(i<te.size()){
                        tutor = te.get(i).getTutor();
                    }
                    else {
                        break;
                    }
                }

                HashMap<String, String> tutorInfo = new HashMap();
                tutorInfo.put("username", udao.getByTutorId(tutor.getId()).get().getUsername());
                tutorInfo.put("state", tutor.getPerson().getState());
                tutorInfo.put("firstName",tutor.getPerson().getFirstName());
                tutorInfo.put("lastName",tutor.getPerson().getLastName());
                tutorInfo.put("city",tutor.getPerson().getCity());
                tutorInfo.put("postalCode",tutor.getPerson().getPostalCode());
                tutorInfo.put("address",tutor.getPerson().getStreetAddress());
                tutorInfo.put("country", tutor.getPerson().getCountry());
                tutorInfo.put("expYears", Integer.toString(tutor.getExpYears()));
                tutorInfo.put("available", Boolean.toString(tutor.isAvailability()));
                for(Subject s : tutor.getExpertise())
                {
                    tutorInfo.put(s.getName(), s.getDescription());
                }
                tutors.add(tutorInfo);
            }
            return Response.ok(tutors).build();
        }
        throw new ForbiddenException("You are not authorize to access this data");
    }

    @GET
    @UnitOfWork
    @Path("/tutor/subjects/{tutorusername}")
    public Response getTutorSubjects(@PathParam("tutorusername") @NotNull String username){
        if(udao.getByUsername(username).isPresent()){
            User user = udao.getByUsername(username).get();
            return Response.ok(user.getTutor().getExpertise()).build();
        }
        throw new ForbiddenException("You are not authorize to access this data");
    }

    @POST
    @UnitOfWork
    @Path("/register")
    public SessionRequest register(@Auth SessionToken auth, RegisterSessionRequest req){
        if(auth.getStudentId()!=0||auth.isStudentManager()){
            if(req.getStudentId() != 0){
                User user = udao.getByUsername(req.getUsername()).get();
                return srdao.create(req.getStudentId(),user.getTutorId(),req.getSubjectId(),req.getDate());
            }
            if(udao.getByUsername(req.getUsername()).isPresent()){
                User user = udao.getByUsername(req.getUsername()).get();
                return srdao.create(auth.getStudentId(),user.getTutorId(),req.getSubjectId(),req.getDate());
            }
        }
        throw new ForbiddenException("You are not authorize to access this data");
    }
}
