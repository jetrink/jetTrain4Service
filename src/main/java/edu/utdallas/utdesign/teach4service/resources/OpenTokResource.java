package edu.utdallas.utdesign.teach4service.resources;

import com.opentok.*;
import com.opentok.exception.OpenTokException;
import edu.utdallas.utdesign.teach4service.T4SConfiguration.OpenTokConfig;
import edu.utdallas.utdesign.teach4service.auth.SessionToken;
import edu.utdallas.utdesign.teach4service.db.SessionDAO;
import edu.utdallas.utdesign.teach4service.db.entities.SessionStatus;
import edu.utdallas.utdesign.teach4service.db.entities.Sessions;
import edu.utdallas.utdesign.teach4service.resources.responses.SessionCreationResponse;
import io.dropwizard.auth.Auth;
import io.dropwizard.hibernate.UnitOfWork;
import lombok.extern.slf4j.Slf4j;
import org.joda.time.DateTime;
import org.joda.time.Minutes;

import javax.annotation.security.RolesAllowed;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.sql.Date;
import java.time.ZonedDateTime;

import static edu.utdallas.utdesign.teach4service.auth.AuthHelper.ROLE_STUDENT;
import static edu.utdallas.utdesign.teach4service.auth.AuthHelper.ROLE_TUTOR;

@Slf4j
@Path("/opentok")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class OpenTokResource
{
    private final OpenTokConfig config;
    private final OpenTok       opentok;
    private final SessionDAO    dao;

    public OpenTokResource(OpenTokConfig config, OpenTok opentok, SessionDAO dao)
    {
        this.config = config;
        this.opentok = opentok;
        this.dao = dao;
    }

    private Session createVideoSession(Sessions t4sSess)
    {
        try
        {
            return opentok.createSession(new SessionProperties.Builder()
                    .mediaMode(MediaMode.ROUTED)
                    .archiveMode(ArchiveMode.MANUAL) // *cough* never
                    .build());

        } catch(OpenTokException e)
        {
            log.error("OpenTok session creation failed! >> " + t4sSess, e);
            throw new ServerErrorException(Response.Status.INTERNAL_SERVER_ERROR, e);
        }
    }

    private void validatetSessionStart(Sessions sess)
    {
        if(sess.getStatus() == SessionStatus.ENDED)
        {
            throw new ForbiddenException("This Session has already ended!");
        }

        if(sess.getStatus() == SessionStatus.SCHEDULED)
        {
            DateTime start = new DateTime(Date.from(sess.getStartDateTime().toInstant()));
            int min = Minutes.minutesBetween(start, DateTime.now()).getMinutes();

            // if before and less than 30 min away
            // or after, and less than 2 hours afterwards
            if((start.isBeforeNow() && min <= 30) || (start.isAfterNow() && min <= (2 * 60)))
            {
                sess.setStatus(SessionStatus.STARTED);
            }
            else
            {
                throw new BadRequestException("Cannot start the session too far away from the scheduled time!");
            }
        }
    }

    @GET
    @UnitOfWork
    @RolesAllowed(ROLE_STUDENT)
    @Path("/{sessionId}/student")
    public SessionCreationResponse joinSessionStudent(@Auth SessionToken auth, @PathParam("sessionId") long sessionId) throws OpenTokException
    {
        Sessions sess = dao.get(sessionId);

        if(sess.getStudentId() != auth.getStudentId())
        {
            throw new ForbiddenException("This student is not part of this session!");
        }

        validatetSessionStart(sess);

        if(sess.getVideoId() == null)
        {
            Session s = createVideoSession(sess);
            sess.setVideoId(s.getSessionId());
        }

        return new SessionCreationResponse(
                config.getApiKey(),
                sess.getVideoId(),
                opentok.generateToken(sess.getVideoId(),
                        new TokenOptions.Builder()
                                .role(Role.PUBLISHER)
                                .expireTime((System.currentTimeMillis() / 1000L) + (4 * 60 * 60)) // in 4 hrs
                                .build()
                ));
    }

    @GET
    @UnitOfWork
    @RolesAllowed(ROLE_TUTOR)
    @Path("{sessionId}/tutor")
    public SessionCreationResponse joinSessionTutor(@Auth SessionToken auth, @PathParam("sessionId") long sessionId) throws OpenTokException
    {
        Sessions sess = dao.get(sessionId);

        if(sess.getTutorId() != auth.getTutorId())
        {
            throw new ForbiddenException("This tutor is not part of this session!");
        }

        validatetSessionStart(sess);

        if(sess.getVideoId() == null)
        {
            Session s = createVideoSession(sess);
            sess.setVideoId(s.getSessionId());
        }

        return new SessionCreationResponse(
                config.getApiKey(),
                sess.getVideoId(),
                opentok.generateToken(sess.getVideoId(),
                        new TokenOptions.Builder()
                                .role(Role.MODERATOR)
                                .expireTime((System.currentTimeMillis() / 1000L) + (4 * 60 * 60)) // in 4 hrs
                                .build()
                ));
    }

    @DELETE
    @UnitOfWork
    @Path("{sessionId}")
    @RolesAllowed({ROLE_TUTOR, ROLE_STUDENT})
    public Response endSession(@Auth SessionToken auth, @PathParam("sessionId") long sessionId) throws OpenTokException
    {
        Sessions sess = dao.get(sessionId);

        validatetSessionStart(sess);

        sess.setVideoId(null);
        sess.setStatus(SessionStatus.ENDED);
        sess.setEndDateTime(ZonedDateTime.now());

        return Response.ok().build();
    }
}
