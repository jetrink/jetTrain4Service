package edu.utdallas.utdesign.teach4service.resources.requests;

import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class ScheduleSetupRequest
{
    @NotNull
    private String day;

    @NotNull
    private String time;
}
