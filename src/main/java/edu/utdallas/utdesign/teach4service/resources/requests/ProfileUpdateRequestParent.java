package edu.utdallas.utdesign.teach4service.resources.requests;

import edu.utdallas.utdesign.teach4service.db.entities.Student;
import lombok.Data;

import java.util.List;

@Data
public class ProfileUpdateRequestParent extends ProfileUpdateRequest
{

    private List<Student> managedStudents;

}
