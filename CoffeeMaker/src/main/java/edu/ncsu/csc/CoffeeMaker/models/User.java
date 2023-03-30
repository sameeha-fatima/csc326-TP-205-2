package edu.ncsu.csc.CoffeeMaker.models;

import java.util.Objects;

import javax.persistence.Entity;
import javax.persistence.Id;

import org.hibernate.validator.constraints.Length;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import edu.ncsu.csc.CoffeeMaker.forms.UserForm;

/**
 * Abstract class representing a User in the CoffeeMaker system. This class is a
 * shared type that is used for all users of the CoffeeMaker system and handles
 * basic functionality such as authenticating the user in the system.
 *
 * @author Ellie Murphy
 *
 */
@Entity
@JsonIgnoreProperties ( value = { "password" } )
public abstract class User extends DomainObject {

    /**
     * Represents the username identifier for a User object, must be between 6
     * and 20 characters
     */
    @Id
    @Length ( min = 2, max = 20 )
    private String username;

    /**
     * Represents the password for a User, must be between 6 an 20 characters
     */
    @Length ( min = 2, max = 20 )
    private String password;

    /**
     * All-argument constructor for User
     *
     * @param username
     *            represents username of User object
     * @param password
     *            represents password of User object
     */
    protected User ( final String username, final String password ) {
        setUsername( username );
        setPassword( password );
    }

    /**
     * Create a new user based off the UserForm
     *
     * @param form
     *            the filled-in user form with user information
     */
    protected User ( final UserForm form ) {
        // TODO
    }

    public String getUsername () {
        return username;
    }

    public String getPassowrd () {
        return password;
    }

    public void setUsername ( final String username ) {
        if ( username == null ) {
            throw new IllegalArgumentException( "User needs a username." );
        }
        if ( !username.matches( "^[a-zA-Z0-9]*$" ) ) {
            throw new IllegalArgumentException( "Invalid characters in username." );
        }

        this.username = username;
    }

    public void setPassword ( final String password ) {
        if ( password == null ) {
            throw new IllegalArgumentException( "User needs a password." );
        }
        if ( !password.matches( "^[a-zA-Z0-9!?$@]*$" ) ) {
            throw new IllegalArgumentException( "Invalid characters in password." );
        }

        this.password = password;
    }

    @Override
    public int hashCode () {
        return Objects.hash( password, username );
    }

    @Override
    public boolean equals ( final Object obj ) {
        if ( this == obj ) {
            return true;
        }
        if ( obj == null ) {
            return false;
        }
        if ( getClass() != obj.getClass() ) {
            return false;
        }
        final User other = (User) obj;
        return Objects.equals( password, other.password ) && Objects.equals( username, other.username );
    }
}
