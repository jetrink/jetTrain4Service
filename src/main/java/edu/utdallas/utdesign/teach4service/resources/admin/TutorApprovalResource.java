package edu.utdallas.utdesign.teach4service.resources.admin;

import com.google.common.collect.Lists;
import edu.utdallas.utdesign.teach4service.auth.SessionToken;
import edu.utdallas.utdesign.teach4service.db.TutorDAO;
import edu.utdallas.utdesign.teach4service.db.entities.Tutor;
import io.dropwizard.auth.Auth;
import io.dropwizard.hibernate.UnitOfWork;

import javax.annotation.security.RolesAllowed;
import javax.validation.constraints.Min;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.List;

import static edu.utdallas.utdesign.teach4service.auth.AuthHelper.ROLE_ADMIN;

@Path("/admin/tutors")
@RolesAllowed(ROLE_ADMIN)
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class TutorApprovalResource
{
    // #########################
    // Tutor approval endpoints
    // #########################
    private final TutorDAO tdao;

    public TutorApprovalResource (TutorDAO tdao)
    {
        this.tdao = tdao;
    }

    @GET
    @UnitOfWork
    public List<Tutor> getAllTutors(@Auth SessionToken auth, @QueryParam("approved") Boolean approved)
    {

        List<Tutor> tutors;

        if(approved == null)
        {
            tutors = tdao.getAuthorizedTutors();
            tutors.addAll(tdao.getUnauthorizedTutors());
        }
        else if(approved)
        {
            // return approved
            tutors = tdao.getAuthorizedTutors();
        }
        else
        {
            tutors = tdao.getUnauthorizedTutors();
            // return unapproved
        }

        return tutors;
    }

    @GET
    @UnitOfWork
    @Path("/{id}/approve")
    public Response approve(@Auth SessionToken auth, @PathParam("id") @Min(1) long tutorId)
    {
        // make the tutor approved. else throw 404.

        Tutor tutor = tdao.get(tutorId);

        if(tutor != null)
        {
            tutor.setApproved(true);
        }
        else
        {
            throw new NotFoundException("Tutor Not found");
        }

        return Response.ok().build();
    }

    @DELETE
    @UnitOfWork
    @Path("/{id}")
    public Response deny(@Auth SessionToken auth, @PathParam("id") @Min(1) long tutorId)
    {

        // make the tutor unapproved. else throw 404.

        Tutor tutor = tdao.get(tutorId);

        if(tutor != null)
        {
            tutor.setApproved(false);
        }
        else
        {
            throw new NotFoundException("Tutor Not found");
        }

        return Response.ok().build();
    }
}
