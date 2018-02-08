package edu.utdallas.utdesign.teach4service.resources.requests;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.hibernate.validator.constraints.NotEmpty;

import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;

@Data
@EqualsAndHashCode(callSuper=false)
public class TutorProfileSetupRequest extends ProfileSetupRequest
{
    @NotNull
    private int expYears;

    @NotEmpty
    private ArrayList <Long> expertise;

    @NotEmpty
    private ArrayList<ScheduleSetupRequest> schedule;



}
