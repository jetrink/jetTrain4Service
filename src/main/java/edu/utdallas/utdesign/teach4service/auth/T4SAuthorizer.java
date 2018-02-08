package edu.utdallas.utdesign.teach4service.auth;

import io.dropwizard.auth.Authorizer;

import static edu.utdallas.utdesign.teach4service.auth.AuthHelper.*;

public class T4SAuthorizer implements Authorizer<SessionToken>
{
    @Override
    public boolean authorize(SessionToken token, String role)
    {
        switch(role)
        {
            case ROLE_ADMIN:
                return token.isAdmin() && token.getUserId() > 0;
            case ROLE_USER:
                return !token.isAdmin() && token.getUserId() > 0;
            case ROLE_TUTOR:
                return token.getTutorId() > 0;
            case ROLE_STUDENT:
                return token.getStudentId() > 0;
            case ROLE_MANAGER:
                return token.isStudentManager();
            default:
                return false;
        }
    }
}
