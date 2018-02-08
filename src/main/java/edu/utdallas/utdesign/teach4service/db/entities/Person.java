package edu.utdallas.utdesign.teach4service.db.entities;

import lombok.Data;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotBlank;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.time.ZonedDateTime;
import java.util.Date;

@Data
@Entity
@Table(name = "Persons")
public class Person
{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @NotBlank
    @Length(max = 500)
    private String firstName;

    @NotBlank
    @Length(max = 500)
    private String lastName;

    @NotNull
    @Temporal(TemporalType.DATE)
    private Date dateOfBirth;

    @NotBlank
    @Length(max = 500)
    private String streetAddress;

    @NotBlank
    @Length(max = 100)
    private String city;

    @NotBlank
    @Length(max = 100)
    private String state;

    @NotBlank
    @Length(max = 100)
    private String country;

    @NotBlank
    @Length(min = 5, max = 10)
    private String postalCode;

    private ZonedDateTime createdOn    = ZonedDateTime.now();
    private ZonedDateTime lastModified = ZonedDateTime.now();
}
