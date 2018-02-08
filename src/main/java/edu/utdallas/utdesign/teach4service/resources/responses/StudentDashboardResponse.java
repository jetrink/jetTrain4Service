package edu.utdallas.utdesign.teach4service.resources.responses;

import edu.utdallas.utdesign.teach4service.db.entities.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

@Data
@EqualsAndHashCode(callSuper=false)
public class StudentDashboardResponse extends DashboardResponse
{
    //Reviews
    private List<StudentReview> studentReviews;

    //Recent Tutors
    private List<Tutor> recentTutors;
}
