package edu.utdallas.utdesign.teach4service.db.entities;

import lombok.Data;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.time.ZonedDateTime;

@Data
@Entity
@Table(name = "TutorReview")
public class TutorReview
{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    private long studentId;
    private long tutorId;

    private long knowledge;
    private long helpful;
    private long friendlyness;
    private long prepared;



    private ZonedDateTime createdOn    = ZonedDateTime.now();
    private ZonedDateTime lastModified = ZonedDateTime.now();

}
