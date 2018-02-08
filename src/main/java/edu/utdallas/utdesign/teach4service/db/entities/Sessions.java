package edu.utdallas.utdesign.teach4service.db.entities;

import lombok.Data;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.time.ZonedDateTime;

@Data
@Entity
@Table(name = "Sessions")
public class Sessions
{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    private long   studentId;
    private long   tutorId;
    private long   subjectId;
    private String description;
    private String videoId;

    @Enumerated(EnumType.STRING)
    private SessionStatus status = SessionStatus.SCHEDULED;

    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "tutorId", insertable = false, updatable = false)
    private Tutor tutor;

    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "studentId", insertable = false, updatable = false)
    private Student student;

    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "subjectId", insertable = false, updatable = false)
    private Subject subject;

    @NotNull
    private ZonedDateTime startDateTime;
    private ZonedDateTime endDateTime;

    private ZonedDateTime createdOn    = ZonedDateTime.now();
    private ZonedDateTime lastModified = ZonedDateTime.now();
}
