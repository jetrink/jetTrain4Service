package edu.utdallas.utdesign.teach4service.db.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.time.ZonedDateTime;

@Data
@Entity
@Table(name = "Schedules")
public class Schedule
{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Enumerated (EnumType.STRING)
    private Days dayAvailable;

    @NotNull
    private String timeAvailableDescription;

    private long tutorId;


    @JsonIgnore
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "tutorId", insertable = false, updatable = false)
    private Tutor tutor;

    private ZonedDateTime createdOn    = ZonedDateTime.now();
    private  ZonedDateTime lastModified = ZonedDateTime.now();

    //scroll up lol

}
