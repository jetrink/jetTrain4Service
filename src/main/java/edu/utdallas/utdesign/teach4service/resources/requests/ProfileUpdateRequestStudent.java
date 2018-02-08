package edu.utdallas.utdesign.teach4service.resources.requests;

import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.validation.constraints.Min;

@Data
@EqualsAndHashCode(callSuper = true)
public class ProfileUpdateRequestStudent extends ProfileUpdateRequest
{
    @Min(1)
    private int gradeLevel;
}
