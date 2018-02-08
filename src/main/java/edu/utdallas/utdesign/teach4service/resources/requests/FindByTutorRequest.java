package edu.utdallas.utdesign.teach4service.resources.requests;

import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class FindByTutorRequest
{
    @NotNull
    private String username;
}
