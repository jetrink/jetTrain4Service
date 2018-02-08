package edu.utdallas.utdesign.teach4service.auth;

import com.auth0.jwt.exceptions.JWTDecodeException;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import edu.utdallas.utdesign.teach4service.auth.csrfAuth.AuthCreds;
import edu.utdallas.utdesign.teach4service.auth.csrfAuth.CSRFAuthFilter;
import edu.utdallas.utdesign.teach4service.auth.csrfAuth.NewAuthCreds;
import io.dropwizard.auth.AuthenticationException;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.joda.time.DateTime;

import java.util.Optional;

@Slf4j
public class T4SAuthenticator implements CSRFAuthFilter.CSRFAuthenticator<SessionToken>
{
    private final AuthHelper helper;

    public T4SAuthenticator(AuthHelper helper)
    {
        this.helper = helper;
    }

    @Override
    public Optional<SessionToken> authenticate(AuthCreds credentials) throws AuthenticationException
    {
        if(credentials.getCookie() == null || credentials.getHeader() == null)
        {
            return Optional.empty();
        }

        String jwt = credentials.getCookie().getValue();

        try
        {
            DecodedJWT decoded = helper.verifyJwt(jwt);

            if(helper.verifyCSRF(decoded, credentials.getHeader()))
            {
                return Optional.of(helper.decodeJwt(decoded));
            }

        } catch(JWTDecodeException e)
        {
            log.debug("JWT decoding failed!", e);

        } catch(JWTVerificationException e)
        {
            log.debug("JWT verification failed!", e);
        }

        return Optional.empty();
    }

    @Override
    public Optional<NewAuthCreds> specifyNewCreds(@NonNull SessionToken old)
    {
        // if it will expire soon, send back a refreshed edition.
        if(new DateTime(old.getExpiration()).minusMinutes(5).isBeforeNow())
        {
            return Optional.of(helper.buildCredentials(old));
        }

        return Optional.empty();
    }
}
