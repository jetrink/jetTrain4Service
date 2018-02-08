package edu.utdallas.utdesign.teach4service.db.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

import javax.persistence.*;
import java.time.ZonedDateTime;

@Data
@Entity
@Table(name = "SessionRequests")
public class SessionRequest
{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    private long requestedFor;
    private long requestedTo;
    private long subjectId;

    @Enumerated(EnumType.STRING)
    private SessionRequestStatus status;
    private ZonedDateTime        sessionDateTime;

    @JsonIgnore
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "subjectId", insertable = false, updatable = false)
    private Subject subject;

    @JsonIgnore
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "requestedTo", insertable = false, updatable = false)
    private Tutor tutor;

    @JsonIgnore
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "requestedFor", insertable = false, updatable = false)
    private Student student;

    private ZonedDateTime createdOn    = ZonedDateTime.now();
    private ZonedDateTime lastModified = ZonedDateTime.now();
}
