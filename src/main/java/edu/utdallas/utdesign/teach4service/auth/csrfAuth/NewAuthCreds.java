package edu.utdallas.utdesign.teach4service.auth.csrfAuth;

import lombok.Data;

import javax.ws.rs.core.NewCookie;

@Data
public class NewAuthCreds
{
    private final NewCookie cookie;
    private final String    header;
}
