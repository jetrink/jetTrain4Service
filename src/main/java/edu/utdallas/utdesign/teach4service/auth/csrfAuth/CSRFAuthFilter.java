package edu.utdallas.utdesign.teach4service.auth.csrfAuth;

import com.google.common.base.Strings;
import io.dropwizard.auth.AuthFilter;
import io.dropwizard.auth.Authenticator;

import javax.annotation.Priority;
import javax.ws.rs.Priorities;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerResponseContext;
import javax.ws.rs.container.ContainerResponseFilter;
import javax.ws.rs.core.Cookie;
import javax.ws.rs.core.SecurityContext;
import java.io.IOException;
import java.security.Principal;
import java.util.Optional;

@Priority(Priorities.AUTHENTICATION)
public class CSRFAuthFilter<P extends Principal> extends AuthFilter<AuthCreds, P> implements ContainerResponseFilter
{
    private final String cookieName, csrfHeader;
    private final CSRFAuthenticator<P> responder;

    private CSRFAuthFilter(String cookieName, String csrfHeader, CSRFAuthenticator<P> responder)
    {
        this.cookieName = cookieName;
        this.csrfHeader = csrfHeader;
        this.responder = responder;
    }

    @Override
    public void filter(ContainerRequestContext requestContext) throws IOException
    {
        // get the cookie & CSRF validation
        Cookie session = requestContext.getCookies().get(cookieName);
        String csrf = Strings.nullToEmpty(requestContext.getHeaderString(csrfHeader));

        AuthCreds credentials = new AuthCreds(session, csrf);

        if(!authenticate(requestContext, credentials, SecurityContext.FORM_AUTH))
        {
            throw new WebApplicationException(unauthorizedHandler.buildResponse(prefix, realm));
        }
    }

    @Override
    @SuppressWarnings("unchecked")
    public void filter(ContainerRequestContext requestContext, ContainerResponseContext responseContext) throws IOException
    {
        String cookieHeader = responseContext.getHeaderString("Set-Cookie");
        if(!Strings.isNullOrEmpty(cookieHeader) && cookieHeader.contains(cookieName))
        {
            // already set. we dont wanna step on toes.
            return;
        }

        SecurityContext sec = requestContext.getSecurityContext();

        if(sec == null)
        {
            return;
        }

        P principal = (P) sec.getUserPrincipal();

        if(principal == null)
        {
            // it never passed auth... why bother?
            return;
        }

        responder.specifyNewCreds(principal).ifPresent((newCreds -> {
            responseContext.getHeaders().add("Set-Cookie", newCreds.getCookie().toString());
            responseContext.getHeaders().add(csrfHeader, newCreds.getHeader());
        }));
    }

    public static class Builder<P extends Principal> extends AuthFilterBuilder<AuthCreds, P, CSRFAuthFilter<P>>
    {
        private String cookieName = "session";
        private String csrfHeader = "X-CSRF";
        private CSRFAuthenticator<P> csrfHandler;

        @Override
        protected CSRFAuthFilter<P> newInstance()
        {
            return new CSRFAuthFilter<P>(cookieName, csrfHeader, csrfHandler);
        }

        public Builder<P> setCookieName(String cookieName)
        {
            this.cookieName = cookieName;
            return this;
        }

        public Builder<P> setCsrfHeader(String header)
        {
            this.csrfHeader = header;
            return this;
        }

        @Override
        @Deprecated
        public AuthFilterBuilder<AuthCreds, P, CSRFAuthFilter<P>> setAuthenticator(Authenticator<AuthCreds, P> authenticator)
        {
            throw new RuntimeException("Deprecated method!");
        }

        public Builder<P> setAuthenticator(CSRFAuthenticator<P> authenticator)
        {
            csrfHandler = authenticator;
            super.setAuthenticator(authenticator);
            return this;
        }
    }

    public interface CSRFAuthenticator<P extends Principal> extends Authenticator<AuthCreds, P>
    {
        /**
         * This function allows the authenticator to specify what credentials are sent back with the response.
         * Returning an empty optional will mean no credentials will be included in the response.
         * This function is only executed for requests that were already originally authenticated
         *
         * @param principal existing Principal
         * @return New credentials to send back in the response
         */
        Optional<NewAuthCreds> specifyNewCreds(P principal);

    }
}
