package edu.utdallas.utdesign.teach4service.resources.requests;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.NotBlank;

@Data
@EqualsAndHashCode(callSuper = true)
public class SignupRequest extends LoginRequest
{
    @Email
    @NotBlank
    private String email;
}
