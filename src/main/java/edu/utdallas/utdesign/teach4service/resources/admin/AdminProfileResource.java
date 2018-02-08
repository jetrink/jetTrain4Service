package edu.utdallas.utdesign.teach4service.resources.admin;

import edu.utdallas.utdesign.teach4service.auth.SessionToken;
import edu.utdallas.utdesign.teach4service.db.AdminDAO;
import edu.utdallas.utdesign.teach4service.db.entities.AdminUser;
import edu.utdallas.utdesign.teach4service.resources.requests.AdminProfileEdit;
import edu.utdallas.utdesign.teach4service.resources.requests.SignupRequest;
import io.dropwizard.auth.Auth;
import io.dropwizard.hibernate.UnitOfWork;
import org.mindrot.jbcrypt.BCrypt;

import javax.annotation.security.RolesAllowed;
import javax.validation.Valid;
import javax.validation.constraints.Min;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.List;
import java.util.Optional;

import static edu.utdallas.utdesign.teach4service.auth.AuthHelper.ROLE_ADMIN;

@Path("/admin/accounts")
@RolesAllowed(ROLE_ADMIN)
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class AdminProfileResource
{
    private final AdminDAO dao;

    public AdminProfileResource(AdminDAO dao)
    {
        this.dao = dao;
    }

    @GET
    @UnitOfWork
    public List<AdminUser> getAllProfiles(@Auth SessionToken auth)
    {
        return dao.getAll();
    }

    @GET
    @UnitOfWork
    @Path("/self")
    public Optional<AdminUser> getSelfProfile(@Auth SessionToken auth)
    {
        return Optional.ofNullable(dao.get(auth.getUserId()));
    }

    @GET
    @UnitOfWork
    @Path("/{userId}")
    public Optional<AdminUser> getAdmin(@Auth SessionToken auth, @PathParam("userId") @Min(1) long userId)
    {
        return Optional.ofNullable(dao.get(userId));
    }

    @POST
    @UnitOfWork
    public AdminUser createAdmin(@Valid SignupRequest signup)
    {
        // normalize lowercase
        signup.setUsername(signup.getUsername().toLowerCase());

        dao.getByUsername(signup.getUsername()).ifPresent((user) -> {
            throw new BadRequestException("Username already taken!");
        });

        return dao.create(signup.getUsername(), signup.getEmail(), signup.getPassword());
    }

    @PUT
    @UnitOfWork
    @Path("/{userId}")
    public Response updateProfile(@Auth SessionToken auth, @PathParam("userId") @Min(1) long userId, @Valid AdminProfileEdit profileEdit)
    {
        AdminUser admin = Optional.ofNullable(dao.get(userId)).orElseThrow(NotFoundException::new);

        if(admin.isSystemUser() && profileEdit.isUpdatePassword())
        {
            throw new BadRequestException("Cannot change the password of the System user");
        }

        if(profileEdit.isUpdateEmail())
        {
            admin.setEmail(profileEdit.getEmail());
        }

        if(profileEdit.isUpdatePassword())
        {
            admin.setPassword(BCrypt.hashpw(profileEdit.getPassword(), BCrypt.gensalt()));
        }

        return Response.ok().build();
    }

    @DELETE
    @UnitOfWork
    @Path("/{userId}")
    public Response deleteAdmin(@Auth SessionToken auth, @PathParam("userId") @Min(1) long userId)
    {
        AdminUser admin = Optional.ofNullable(dao.get(userId)).orElseThrow(NotFoundException::new);

        if(admin.isSystemUser())
        {
            throw new ForbiddenException("Can never delete the System user!");
        }

        dao.delete(admin);

        return Response.ok().build();
    }
}
