package edu.utdallas.utdesign.teach4service.resources.responses;

import com.google.common.collect.Lists;
import edu.utdallas.utdesign.teach4service.db.entities.Sessions;
import edu.utdallas.utdesign.teach4service.db.entities.Student;
import edu.utdallas.utdesign.teach4service.db.entities.StudentReview;
import edu.utdallas.utdesign.teach4service.db.entities.Tutor;
import lombok.Data;

import java.util.List;

@Data
public class ParentDashboardResponse
{
    //List Of managed Students
    private List<Student> managedStudents = Lists.newArrayList();

    // Calendar
    private List<Sessions> sessions = Lists.newArrayList();

    //Reviews
    private List<StudentReview> studentReviews = Lists.newArrayList();

    //Recent Tutors
    private List<List<Tutor>> recentTutors = Lists.newArrayList();
}
