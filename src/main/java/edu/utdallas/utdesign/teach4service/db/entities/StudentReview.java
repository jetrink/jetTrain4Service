package edu.utdallas.utdesign.teach4service.db.entities;

import lombok.Data;
import lombok.NonNull;

import javax.persistence.*;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.time.ZonedDateTime;

@Data
@Entity
@Table(name = "StudentReview")
public class StudentReview
{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    private long studentId;
    private long tutorId;

    private long effort;
    private long respectfullness;
    private long comprehension;
    private long preperation;
    private long participation;


    private ZonedDateTime createdOn    = ZonedDateTime.now();
    private ZonedDateTime lastModified = ZonedDateTime.now();

}
