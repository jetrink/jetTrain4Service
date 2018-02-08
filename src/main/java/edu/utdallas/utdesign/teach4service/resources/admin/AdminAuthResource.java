package edu.utdallas.utdesign.teach4service.resources.admin;

import edu.utdallas.utdesign.teach4service.T4SConfiguration.SystemUserConfig;
import edu.utdallas.utdesign.teach4service.auth.AuthHelper;
import edu.utdallas.utdesign.teach4service.auth.SessionToken;
import edu.utdallas.utdesign.teach4service.auth.csrfAuth.NewAuthCreds;
import edu.utdallas.utdesign.teach4service.db.AdminDAO;
import edu.utdallas.utdesign.teach4service.db.entities.AdminUser;
import edu.utdallas.utdesign.teach4service.resources.UserResource;
import edu.utdallas.utdesign.teach4service.resources.requests.LoginRequest;
import io.dropwizard.auth.Auth;
import io.dropwizard.hibernate.UnitOfWork;
import org.mindrot.jbcrypt.BCrypt;

import javax.annotation.security.PermitAll;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.time.ZonedDateTime;

@Path("/admin/auth")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class AdminAuthResource
{
    private final AdminDAO         dao;
    private final AuthHelper       authHelper;
    private final SystemUserConfig config;

    public AdminAuthResource(AdminDAO dao, AuthHelper authHelper, SystemUserConfig config)
    {
        this.dao = dao;
        this.authHelper = authHelper;
        this.config = config;
    }

    @POST
    @UnitOfWork
    @Path("/login")
    public Response login(@Valid LoginRequest login, @Context HttpServletRequest req)
    {
        login.setUsername(login.getUsername().toLowerCase());

        AdminUser user = dao.getByUsername(login.getUsername()).orElseThrow(() -> new ForbiddenException("No such user!"));

        boolean passwordOk = false;
        if(user.isSystemUser())
        {
            if(!config.isEnabled())
            {
                throw new ForbiddenException("Account disabled!");
            }

            passwordOk = config.getPassword().equals(login.getPassword());
        }
        else
        {
            passwordOk = BCrypt.checkpw(login.getPassword(), user.getPassword());
        }

        if(passwordOk)
        {
            // set activity stuff
            user.setLastLogin(ZonedDateTime.now());
            user.setLastLoginIp(UserResource.extractIp(req));

            NewAuthCreds creds = authHelper.buildCredentials(user);

            return Response.ok(user)
                    .cookie(creds.getCookie())
                    .header(AuthHelper.CSRF_HEADER, creds.getHeader())
                    .build();
        }
        else
        {
            throw new ForbiddenException("Invalid password!");
        }
    }

    @GET
    @PermitAll
    @Path("/logout")
    public Response logout(@Auth SessionToken auth)
    {
        // Yeah, exactly the same as the one in the UserResource.. here for convenience really.
        return Response.ok()
                .cookie(authHelper.buildLogoutCookie())
                .build();
    }
}
