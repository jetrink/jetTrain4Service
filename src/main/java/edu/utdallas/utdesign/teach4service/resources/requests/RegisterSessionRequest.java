package edu.utdallas.utdesign.teach4service.resources.requests;

import lombok.Data;

import javax.validation.constraints.NotNull;
import java.time.ZonedDateTime;

@Data
public class RegisterSessionRequest
{
    @NotNull
    private String        username;
    private int studentId;
    private long          subjectId;
    private ZonedDateTime date;
}
