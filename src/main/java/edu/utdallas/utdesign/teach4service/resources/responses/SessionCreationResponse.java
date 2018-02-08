package edu.utdallas.utdesign.teach4service.resources.responses;

import lombok.Data;

@Data
public class SessionCreationResponse
{
    private final int    apiKey;
    private final String sessionId;
    private final String token;
}
