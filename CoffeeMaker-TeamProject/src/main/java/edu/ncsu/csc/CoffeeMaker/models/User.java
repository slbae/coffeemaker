package edu.ncsu.csc.CoffeeMaker.models;

import java.io.Serializable;
import java.util.Objects;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

/**
 * User for the coffee maker. User is tied to the database using Hibernate
 * libraries. See UserRepository and UserService for the other two pieces used
 * for database support.
 *
 * @author Caleb Twigg
 * @author Tyler Bradshaw
 * @author Helen O'Connell
 * @author Sylbi Bae
 *
 */
@Entity
public class User extends DomainObject {

    /**
     * User's ID
     */
    @Id
    @GeneratedValue
    private Long    id;

    /**
     * User's username
     */
    private String  username;

    /**
     * User's password
     */
    private String  password;

    /**
     * User's role
     */
    private String  role;

    /**
     * Is the user logged in
     */
    private Boolean loggedIn;

    /**
     * Is the user logged in
     *
     * @return true if so
     */
    public Boolean isLoggedIn () {
        return loggedIn;
    }

    /**
     * Log the user in and set boolean to true
     *
     */
    public void logIn () {
        this.loggedIn = true;
    }

    /**
     * Log out
     */
    public void logOut () {
        this.loggedIn = false;
    }

    /**
     * Creates a new User (Customer or Staff). Sets
     *
     * @param username
     *            user's username
     * @param password
     *            user's password
     * @param role
     *            customer or staff user role
     */
    public User ( final String username, final String password, final String role ) {
        setUsername( username );
        setPassword( password );
        setRole( role );

        loggedIn = false;

    }

    /**
     * Constructor for an anonymous Customer.
     */
    public User () {

    }

    /**
     * Gets username.
     *
     * @return username the user's username
     */
    public String getUsername () {
        return username;
    }

    /**
     * Sets username.
     *
     * @param username
     *            the user's username
     */
    public void setUsername ( final String username ) {
        if ( username != null && username.length() >= 1 ) {
            this.username = username;
        }
        else {
            throw new IllegalArgumentException( "Invalid username" );
        }
    }

    /**
     * Gets password.
     *
     * @return password the users password
     */
    public String getPassword () {
        return password;
    }

    /**
     * Sets password.
     *
     * @param password
     *            the users password
     */
    public void setPassword ( final String password ) {
        if ( password != null && password.length() >= 1 ) {
            this.password = password;
        }
        else {
            throw new IllegalArgumentException( "Invalid password" );
        }
    }

    @Override
    public Serializable getId () {
        return id;
    }

    /**
     * Sets id (used by Hibernate).
     *
     * @param id
     *            the id of the user
     */
    public void setId ( final Long id ) {
        this.id = id;
    }

    /**
     * Gets user role (Customer or Staff).
     *
     * @return role the role of the user
     */
    public String getRole () {
        return role;
    }

    /**
     * Sets user role (Customer or Staff).
     *
     * @param role
     *            the role of the user
     */
    public void setRole ( final String role ) {
        if ( role != null && ( "Staff".equals( role ) || "Customer".equals( role ) ) ) {
            this.role = role;
        }
        else {
            throw new IllegalArgumentException( "Invalid role" );
        }
    }

    @Override
    public String toString () {
        return "User [username=" + username + ", password=" + password + ", id=" + id + ", role=" + role + "]";
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
        return Objects.equals( id, other.id ) && Objects.equals( password, other.password )
                && Objects.equals( role, other.role ) && Objects.equals( username, other.username );
    }

    @Override
    public int hashCode () {
        return Objects.hash( id, loggedIn, password, role, username );
    }

}
