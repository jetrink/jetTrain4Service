package edu.utdallas.utdesign.teach4service.db.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

import javax.persistence.*;
import java.time.ZonedDateTime;

@Data
@Entity
@Table(name = "AdminUsers")
public class AdminUser
{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    private String username;
    private String email;

    @JsonIgnore
    private String password;

    @Enumerated(EnumType.STRING)
    private UserStatus status = UserStatus.ACTIVE;

    private ZonedDateTime lastLogin    = ZonedDateTime.now();
    private ZonedDateTime createdOn    = ZonedDateTime.now();
    private ZonedDateTime lastModified = ZonedDateTime.now();
    private String lastLoginIp;

    public boolean isSystemUser()
    {
        return id == 1;
    }

    public void setPassword(String password)
    {
        if(isSystemUser())
        {
            throw new RuntimeException("Cannot change the password of the system user.");
        }
        else
        {
            this.password = password;
        }
    }
}
