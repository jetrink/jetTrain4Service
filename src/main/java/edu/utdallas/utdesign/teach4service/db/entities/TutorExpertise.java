package edu.utdallas.utdesign.teach4service.db.entities;

import lombok.Data;

import javax.persistence.*;
import java.time.ZonedDateTime;

@Data
@Entity
@Table(name = "TutorExpertise")
public class TutorExpertise
{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "tutorId", insertable = false, updatable = false)
    private Tutor tutor;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "subjectId", insertable = false, updatable = false)
    private Subject subject;

    private Long tutorId;

    private Long subjectId;

    int rating = 0;

    private ZonedDateTime createdOn = ZonedDateTime.now();
}
