package edu.utdallas.utdesign.teach4service.resources.requests;

import lombok.Data;

import java.util.Date;

@Data
public class ProfileUpdateRequest
{
    private String firstName;

    private String lastName;

    private Date dateOfBirth;

    private String streetAddress;

    private String city;

    private String state;

    private String country;

    private String postalCode;
}
