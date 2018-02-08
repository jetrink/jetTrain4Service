package edu.utdallas.utdesign.teach4service.resources.requests;

import lombok.Data;

@Data
public class FindByTutorNameRequest
{
    private String firstName;
    private String lastName;
}
