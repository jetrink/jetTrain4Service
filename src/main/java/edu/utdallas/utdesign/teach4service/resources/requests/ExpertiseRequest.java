package edu.utdallas.utdesign.teach4service.resources.requests;

import lombok.Data;
import org.hibernate.validator.constraints.NotBlank;

@Data
public class ExpertiseRequest
{
    @NotBlank
    private String name;

    @NotBlank
    private String field;

    @NotBlank
    private String description;
}
