package edu.utdallas.utdesign.teach4service.db;

import edu.utdallas.utdesign.teach4service.db.entities.Person;
import edu.utdallas.utdesign.teach4service.db.entities.User;
import io.dropwizard.hibernate.AbstractDAO;
import org.hibernate.SessionFactory;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
import java.util.Optional;

public class PersonDAO extends AbstractDAO<Person>
{
    public PersonDAO(SessionFactory factory){
        super(factory);
    }

    @Override
    public Person get(Serializable id)
    {
        return super.get(id);
    }

    public Optional<Person> getExistingPerson(String firstName, String lastName, Date dob, String address, String city, String state, String country, String postalCode)
    {
        return currentSession().createNativeQuery("SELECT * FROM `Persons` WHERE `firstName` = :firstname and `lastName` = :lastname" +
                " and `dateOfBirth` = :dob and `streetAddress` = :address and `city` = :city " +
                "and `state` = :state and `country` = :country and `postalCode` = :postalCode", Person.class)
                .setParameter("firstname", firstName)
                .setParameter("lastname",lastName)
                .setParameter("dob",dob)
                .setParameter("address",address)
                .setParameter("city",city)
                .setParameter("state",state)
                .setParameter("country",country)
                .setParameter("postalCode",postalCode)
                .uniqueResultOptional();
    }



    public Person create(String firstName, String lastName, Date dob, String address, String city, String state, String country, String postalCode){
        if(!getExistingPerson(firstName,lastName,dob,address,city,state,country,postalCode).isPresent()){
            Person person = new Person();
            person.setFirstName(firstName);
            person.setLastName(lastName);

            person.setDateOfBirth(dob);
            person.setStreetAddress(address);
            person.setCity(city);
            person.setState(state);
            person.setCountry(country);
            person.setPostalCode(postalCode);
            return persist(person);
        }
        else{
            return getExistingPerson(firstName,lastName,dob,address,city,state,country,postalCode).get();
        }


    }
}
