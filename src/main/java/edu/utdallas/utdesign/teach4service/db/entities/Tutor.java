package edu.utdallas.utdesign.teach4service.db.entities;

import com.google.common.collect.Sets;
import lombok.Data;

import javax.persistence.*;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.time.ZonedDateTime;
import java.util.Set;

@Data
@Entity
@Table(name = "Tutors")
public class Tutor
{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private boolean approved = false;
    private int     expYears = 0;
    private boolean availability = false;

    @Valid
    @NotNull
    @JoinColumn(name = "personId")
    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    private Person person;

    private ZonedDateTime createdOn    = ZonedDateTime.now();
    private ZonedDateTime lastModified = ZonedDateTime.now();

    @ManyToMany
    @JoinTable(name = "TutorExpertise",
               joinColumns = {@JoinColumn(name = "tutorId")},
               inverseJoinColumns = {@JoinColumn(name = "subjectId")}
    )
    private Set<Subject> expertise = Sets.newHashSet();
}
