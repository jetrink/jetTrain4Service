package edu.utdallas.utdesign.teach4service.resources.responses;

import edu.utdallas.utdesign.teach4service.db.entities.Student;
import edu.utdallas.utdesign.teach4service.db.entities.TutorReview;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

@Data
@EqualsAndHashCode(callSuper = true)
public class TutorDashboardResponse extends DashboardResponse
{
    //Reviews
    private List<TutorReview> tutorReviews;

    //Recent Students
    private List<Student> recentStudents;

    //ToggleOnline
    private boolean isOnline;
}
