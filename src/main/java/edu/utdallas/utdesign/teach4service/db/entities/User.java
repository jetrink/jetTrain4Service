package edu.utdallas.utdesign.teach4service.db.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.google.common.collect.Sets;
import lombok.Data;

import javax.persistence.*;
import java.time.ZonedDateTime;
import java.util.Set;

@Data
@Entity
@Table(name = "Users",
       uniqueConstraints = {
               @UniqueConstraint(columnNames = {"username"}),
               @UniqueConstraint(columnNames = {"email"})
       }
)
public class User
{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    private String username;
    private String email;

    @JsonIgnore
    private String password;

    private Long tutorId;
    private Long studentId;
    private Long managerPersonId;

    @Enumerated(EnumType.STRING)
    private UserStatus status = UserStatus.ACTIVE;

    @JsonIgnore
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "tutorId", insertable = false, updatable = false)
    private Tutor tutor;

    @JsonIgnore
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "studentId", insertable = false, updatable = false)
    private Student student;

    private boolean       isStudentManager = false;
    private ZonedDateTime lastLogin        = ZonedDateTime.now();
    private ZonedDateTime createdOn        = ZonedDateTime.now();
    private ZonedDateTime lastModified     = ZonedDateTime.now();
    private String lastLoginIp;

    @JsonIgnore
    @OneToMany(mappedBy = "managerId")
    private Set<Student> managedStudents = Sets.newHashSet();

}