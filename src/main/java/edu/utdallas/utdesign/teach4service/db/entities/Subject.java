package edu.utdallas.utdesign.teach4service.db.entities;

import lombok.Data;

import javax.persistence.*;
import java.time.ZonedDateTime;

@Data
@Entity
@Table(name = "Subjects",
       uniqueConstraints = {
               @UniqueConstraint(columnNames = {"name"})
       }
)
public class Subject
{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Enumerated (EnumType.STRING)
    private SubjectField field;

    private String name;
    private String description;

    private ZonedDateTime createdOn = ZonedDateTime.now();
}
