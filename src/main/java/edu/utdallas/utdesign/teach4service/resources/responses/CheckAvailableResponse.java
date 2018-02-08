package edu.utdallas.utdesign.teach4service.resources.responses;

import lombok.Data;

@Data
public class CheckAvailableResponse
{
    private final String  message;
    private final boolean available;
}
