package edu.utdallas.utdesign.teach4service.resources.requests;

import edu.utdallas.utdesign.teach4service.db.entities.SessionRequestStatus;
import lombok.Data;

import javax.validation.constraints.Min;

@Data
public class SessionResponseRequest
{
    @Min(1)
    private long                 requestId;
    private SessionRequestStatus status;
}
