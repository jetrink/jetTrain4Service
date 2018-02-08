package edu.utdallas.utdesign.teach4service.auth;

import com.fasterxml.jackson.annotation.JsonIgnore;
import edu.utdallas.utdesign.teach4service.db.entities.UserStatus;
import lombok.Data;

import java.security.Principal;
import java.util.Date;

@Data
public class SessionToken implements Principal
{
    private long    userId;
    private long    studentId;
    private long    tutorId;
    private boolean isStudentManager;
    private Date    expiration;

    private boolean isAdmin;

    private UserStatus status;

    @Override
    @JsonIgnore
    public String getName()
    {
        if(isAdmin)
        {
            return "Admin" + userId;
        }
        else
        {
            return "User" + userId;
        }
    }
}
