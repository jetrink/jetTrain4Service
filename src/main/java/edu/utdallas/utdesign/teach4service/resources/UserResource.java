package edu.utdallas.utdesign.teach4service.resources;

import com.google.common.base.Strings;
import edu.utdallas.utdesign.teach4service.auth.AuthHelper;
import edu.utdallas.utdesign.teach4service.auth.SessionToken;
import edu.utdallas.utdesign.teach4service.auth.csrfAuth.NewAuthCreds;
import edu.utdallas.utdesign.teach4service.db.UserDAO;
import edu.utdallas.utdesign.teach4service.db.entities.User;
import edu.utdallas.utdesign.teach4service.db.entities.UserStatus;
import edu.utdallas.utdesign.teach4service.resources.requests.LoginRequest;
import edu.utdallas.utdesign.teach4service.resources.requests.SignupRequest;
import edu.utdallas.utdesign.teach4service.resources.responses.CheckAvailableResponse;
import io.dropwizard.auth.Auth;
import io.dropwizard.hibernate.UnitOfWork;
import org.hibernate.validator.constraints.NotBlank;
import org.mindrot.jbcrypt.BCrypt;

import javax.annotation.security.PermitAll;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import javax.validation.constraints.Min;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.time.ZonedDateTime;
import java.util.Optional;

@Path("/users")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class UserResource
{
    private final UserDAO    dao;
    private final AuthHelper authHelper;

    public UserResource(UserDAO dao, AuthHelper authHelper)
    {
        this.dao = dao;
        this.authHelper = authHelper;
    }

    @GET
    @PermitAll
    @UnitOfWork
    @Path("/{userId}")
    public User getUser(@Auth SessionToken auth, @PathParam("userId") @Min(1) long userId)
    {
        if(auth.getUserId() == userId)
        {
            return dao.get(userId);
        }
        // TODO: allow to see managed users too.
        else
        {
            throw new ForbiddenException("Can only get information for yourself");
        }
    }

    @GET
    @PermitAll
    @UnitOfWork
    @Path("/self")
    public Optional<User> getSelf(@Auth SessionToken auth)
    {
        return Optional.ofNullable(dao.get(auth.getUserId()));
    }

    @GET
    @UnitOfWork
    @Path("/checkUsername")
    public CheckAvailableResponse checkUsername(@QueryParam("username") @NotBlank String username)
    {
        username = username.toLowerCase();

        if(dao.getByUsername(username).isPresent())
        {
            return new CheckAvailableResponse("Username Taken", false);
        }
        else
        {
            return new CheckAvailableResponse("Username Available", true);
        }
    }

    @POST
    @UnitOfWork
    @Path("/signup")
    public Response signup(@Valid SignupRequest signup, @Context HttpServletRequest req)
    {
        signup.setUsername(signup.getUsername().toLowerCase());

        dao.getByUsername(signup.getUsername()).ifPresent((user) -> {
            throw new BadRequestException("Username already taken!");
        });

        dao.getByEmail(signup.getEmail()).ifPresent((user) -> {
            throw new BadRequestException("Email already taken!");
        });

        User user = dao.create(signup.getUsername(), signup.getEmail(), signup.getPassword(), extractIp(req));

        NewAuthCreds creds = authHelper.buildCredentials(user);

        return Response.ok(user)
                .cookie(creds.getCookie())
                .header(AuthHelper.CSRF_HEADER, creds.getHeader())
                .build();
    }

    @POST
    @UnitOfWork
    @Path("/login")
    public Response login(@Valid LoginRequest login, @Context HttpServletRequest req)
    {
        login.setUsername(login.getUsername().toLowerCase());

        User user = dao.getByUsername(login.getUsername()).orElseThrow(() -> new ForbiddenException("No such user!"));

        if(user.getStatus() == UserStatus.DISABLED)
        {
            throw new ForbiddenException("Account disabled!");
        }

        if(BCrypt.checkpw(login.getPassword(), user.getPassword()))
        {
            // set activity stuff
            user.setLastLogin(ZonedDateTime.now());
            user.setLastLoginIp(extractIp(req));

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
        return Response.ok()
                .cookie(authHelper.buildLogoutCookie())
                .build();
    }

    public static String extractIp(HttpServletRequest req)
    {
        String ip = req.getRemoteAddr();
        String forwarded = req.getHeader("X-Forwarded-For");
        if(!Strings.isNullOrEmpty(forwarded))
        {
            int index = forwarded.indexOf(',');
            if(index > 0)
            {
                ip = forwarded.substring(0, index).trim();
            }
        }

        return ip;
    }
}
