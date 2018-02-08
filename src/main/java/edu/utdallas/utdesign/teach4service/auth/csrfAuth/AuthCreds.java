package edu.utdallas.utdesign.teach4service.auth.csrfAuth;

import lombok.Data;

import javax.ws.rs.core.Cookie;

@Data
public class AuthCreds
{
    private final Cookie cookie;
    private final String header;
}
