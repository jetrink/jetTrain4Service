package edu.utdallas.utdesign.teach4service.resources.requests;

import lombok.Data;
import org.hibernate.validator.constraints.Email;

@Data
public class AdminProfileEdit
{
    private boolean updateEmail;
    @Email
    private String  email;

    private boolean updatePassword;
    private String  password;
}
