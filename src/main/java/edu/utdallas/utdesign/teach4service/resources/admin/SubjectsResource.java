package edu.utdallas.utdesign.teach4service.resources.admin;

import com.google.common.collect.Lists;
import edu.utdallas.utdesign.teach4service.auth.SessionToken;
import edu.utdallas.utdesign.teach4service.db.SubjectDAO;
import edu.utdallas.utdesign.teach4service.db.entities.Subject;
import edu.utdallas.utdesign.teach4service.db.entities.SubjectField;
import edu.utdallas.utdesign.teach4service.resources.requests.SubjectRequest;
import io.dropwizard.auth.Auth;
import io.dropwizard.hibernate.UnitOfWork;

import javax.annotation.security.RolesAllowed;
import javax.validation.Valid;
import javax.validation.constraints.Min;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.util.List;

import static edu.utdallas.utdesign.teach4service.auth.AuthHelper.ROLE_ADMIN;

@Path("/admin/subjects")
@RolesAllowed(ROLE_ADMIN)
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class SubjectsResource
{
    // #########################
    // Subject management endpoints
    // #########################
    private SubjectDAO subdao;



    public SubjectsResource(SubjectDAO subdao)
    {
        this.subdao = subdao;
    }

    @GET
    @UnitOfWork
    public List<Subject> getAllSubjects(@Auth SessionToken auth)
    {
        List<Subject> subjects;

        subjects = subdao.getSubjects();

        return subjects;
    }

    @POST
    @UnitOfWork
    public Subject createSubject(@Auth SessionToken auth, @Valid SubjectRequest subject)
    {


        return subdao.create(subject.getName(),subject.getField(), subject.getDescription());

    }

    @PUT
    @UnitOfWork
    @Path("/{id}")
    public Subject editSubject(@Auth SessionToken auth, SubjectRequest edits, @PathParam("id") @Min(1) long subjectId)
    {
        Subject subject = subdao.get(subjectId);

        if(subject != null)
        {
            if(edits.getName() != null)
            {subject.setName(edits.getName());}

            if(edits.getDescription() != null)
            {subject.setDescription(edits.getDescription());}

            if(edits.getField() != null)
            {subject.setField(SubjectField.valueOf(edits.getField()));}

            return subject;
        }
        throw new NotFoundException("Subject Not found");



    }

    @DELETE
    @UnitOfWork
    @Path("/{id}")
    @RolesAllowed(ROLE_ADMIN)
    public void deleteSubject(@Auth SessionToken auth, @PathParam("id") @Min(1) long subjectId)
    {



        Subject leavingSubject = subdao.get(subjectId);

        if(leavingSubject != null)
        {
            subdao.delete(leavingSubject);

        }
        else
        {
            throw new NotFoundException("Subject Not Found");
        }
    }
}
