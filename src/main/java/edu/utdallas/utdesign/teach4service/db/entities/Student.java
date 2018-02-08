package edu.utdallas.utdesign.teach4service.db.entities;

import lombok.Data;

import javax.persistence.*;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.time.ZonedDateTime;

@Data
@Entity
@Table(name = "Students")
public class Student
{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    private int gradeLevel = 0;

    @Valid
    @NotNull
    @JoinColumn(name = "personId")
    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    private Person person;

    private Long managerId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "managerId", insertable = false, updatable = false)
    private User manager;

    private ZonedDateTime createdOn    = ZonedDateTime.now();
    private ZonedDateTime lastModified = ZonedDateTime.now();

}
