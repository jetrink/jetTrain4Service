package edu.utdallas.utdesign.teach4service.resources.requests;

import com.google.common.collect.Sets;
import edu.utdallas.utdesign.teach4service.db.entities.Schedule;
import edu.utdallas.utdesign.teach4service.db.entities.Subject;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;

@Data
@EqualsAndHashCode(callSuper=false)
public class ProfileUpdateRequestTutor extends ProfileUpdateRequest
{

    private int expYears;

    private Set<Subject> expertise = Sets.newHashSet();

    private List<Schedule> schedule;
}
